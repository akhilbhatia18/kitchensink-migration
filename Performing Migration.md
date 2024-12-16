## Migration from Jakarta EE to Spring Boot

### Running & Understanding the Initial Project
To begin, the original project was set up and executed locally:
- The repository was cloned.
- The project was built using its existing configurations.
- JBOSS was installed as the application server.
- Read the code and understand the functionality.

### Updating Dependencies
The `pom.xml` file was modified to include the necessary dependencies for Spring Boot, replacing those used in the Jakarta EE framework.

### Refactoring Annotations and Components
- Jakarta EE annotations were replaced with their Spring Boot equivalents.
- EJB components were refactored into Spring Beans, utilizing `@Service` and `@Repository` annotations.
- Dependency injection was transitioned from `@Inject` to constructor-based injection for better testability and alignment with Spring practices.

### Refactoring Controllers
REST controllers built with Jakarta EE were migrated to Spring Boot using the `@RestController` annotation, along with Spring's request mapping and exception handling features.

### Refactoring Persistence Layer
- Jakarta EE's JPA configuration was replaced with Spring Data JPA.
- Entity classes and repositories were updated to use Spring Data JPA's annotations and repository interfaces for simplified database operations.

### Updating Test Cases
The existing test cases were rewritten to leverage Spring Boot's testing framework, enabling seamless integration and unit testing using tools like JUnit and Mockito.