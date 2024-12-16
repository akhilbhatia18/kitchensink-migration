package com.quickstarts.kitchensink.migrator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class EntityMigrationTool {

    /**
     * Main migration method to convert JPA entities to Spring Boot MongoDB entities
     *
     * @param sourceDir Directory containing original JPA entities
     * @param targetDir Directory to output migrated entities
     * @throws IOException If file operations fail
     */
    public void migrateEntities(String sourceDir, String targetDir) throws IOException {
        // Ensure target directory exists
        Files.createDirectories(Paths.get(targetDir));

        // Find all Java files in the source directory
        List<Path> jpaEntityFiles = findJPAEntities(sourceDir);

        // Migrate each entity
        for (Path entityPath : jpaEntityFiles) {
            CompilationUnit migratedEntity = migrateEntityFile(entityPath);

            // Write migrated entity to target directory
            writeEntityToFile(migratedEntity, targetDir);
        }

        System.out.println("Migration completed. Migrated " + jpaEntityFiles.size() + " entities.");
    }

    /**
     * Find all JPA entity files in the source directory
     *
     * @param sourceDir Source directory to search
     * @return List of paths to JPA entity files
     * @throws IOException If file searching fails
     */
    private List<Path> findJPAEntities(String sourceDir) throws IOException {
        return Files.walk(Paths.get(sourceDir))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .filter(this::isJPAEntity)
                .collect(Collectors.toList());
    }

    /**
     * Check if a file represents a JPA entity
     *
     * @param path Path to the Java file
     * @return true if the file contains JPA entity annotations
     */
    private boolean isJPAEntity(Path path) {
        try {
            String content = Files.readString(path);
            return content.contains("@Entity") ||
                    content.contains("jakarta.persistence.Entity") ||
                    content.contains("javax.persistence.Entity");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Migrate a single entity file from JPA to Spring Boot MongoDB
     *
     * @param entityPath Path to the original entity file
     * @return Migrated CompilationUnit
     * @throws IOException If file reading fails
     */
    private CompilationUnit migrateEntityFile(Path entityPath) throws IOException {
        // Parse the original entity file
        CompilationUnit cu = StaticJavaParser.parse(entityPath);

        // Find the main class
        ClassOrInterfaceDeclaration entityClass = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .orElseThrow(() -> new IllegalArgumentException("No class found in " + entityPath));

        // Update imports
        updateImports(cu);

        // Transform class annotations
        transformClassAnnotations(entityClass);

        // Transform field annotations
        transformFieldAnnotations(entityClass);

        return cu;
    }

    /**
     * Update import statements for MongoDB and Lombok
     *
     * @param cu Compilation Unit to update
     */
    private void updateImports(CompilationUnit cu) {
        // Remove JPA imports
        cu.getImports().removeIf(imp ->
                imp.getName().asString().startsWith("jakarta.persistence") ||
                        imp.getName().asString().startsWith("javax.persistence")
        );

        // Add new imports
        cu.addImport("org.springframework.data.mongodb.core.mapping.Document");
        cu.addImport("org.springframework.data.annotation.Id");
        cu.addImport("org.springframework.data.mongodb.core.index.Indexed");

        // Lombok imports
        cu.addImport("lombok.Data");
        cu.addImport("lombok.NoArgsConstructor");
        cu.addImport("lombok.AllArgsConstructor");

        // Validation imports
        cu.addImport("jakarta.validation.constraints.*");
    }

    /**
     * Transform class-level JPA annotations to MongoDB annotations
     *
     * @param entityClass Class to transform
     */
    private void transformClassAnnotations(ClassOrInterfaceDeclaration entityClass) {
        // Remove existing JPA entity annotations
        entityClass.getAnnotations().removeIf(ann ->
                ann.getName().asString().equals("Entity") ||
                        ann.getName().asString().equals("Table") ||
                        ann.getName().asString().equals("XmlRootElement")
        );

        // Add MongoDB and Lombok annotations
        entityClass.addAnnotation(new MarkerAnnotationExpr("Document"));
        entityClass.addAnnotation(new MarkerAnnotationExpr("Data"));
        entityClass.addAnnotation(new MarkerAnnotationExpr("NoArgsConstructor"));
        entityClass.addAnnotation(new MarkerAnnotationExpr("AllArgsConstructor"));
    }

    /**
     * Transform field-level JPA annotations to MongoDB and validation annotations
     *
     * @param entityClass Class containing fields to transform
     */
    private void transformFieldAnnotations(ClassOrInterfaceDeclaration entityClass) {
        for (FieldDeclaration field : entityClass.getFields()) {
            // Transform ID annotation
            if (field.getAnnotationByName("Id").isPresent()) {
                field.getAnnotations().removeIf(ann ->
                        ann.getName().asString().equals("Id") ||
                                ann.getName().asString().equals("GeneratedValue")
                );
                field.addAnnotation(new MarkerAnnotationExpr("Id"));

                // Change ID type to String if it's numeric
/*                if (field.getElementType().asString().equals("Long")) {
                    field.set
                    field.setElementType(StaticJavaParser.parseType("String"));
                }*/
            }

            // Transform unique constraint
            if (field.getAnnotationByName("Column").isPresent()) {
                field.getAnnotations().removeIf(ann ->
                        ann.getName().asString().equals("Column")
                );
                field.addAnnotation(new MarkerAnnotationExpr("Indexed"));
            }
        }
    }

    /**
     * Write migrated entity to a file in the target directory
     *
     * @param migratedEntity Migrated compilation unit
     * @param targetDir      Target directory for output
     * @throws IOException If file writing fails
     */
    private void writeEntityToFile(CompilationUnit migratedEntity, String targetDir) throws IOException {
        String className = migratedEntity.findFirst(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .orElseThrow();

        Path outputPath = Paths.get(targetDir, className + ".java");

        try (FileWriter writer = new FileWriter(outputPath.toFile())) {
            writer.write(migratedEntity.toString());
        }
    }

    /**
     * Main method to demonstrate usage
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        EntityMigrationTool migrationTool = new EntityMigrationTool();
        try {
            migrationTool.migrateEntities("src/main/java/com/quickstarts/kitchensink/Member.java", "src/main/java/com/quickstarts/kitchensink/Member1.java");
        } catch (IOException e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}