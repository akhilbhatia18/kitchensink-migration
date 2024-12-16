package com.quickstarts.kitchensink.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "members")
@Schema(description = "Member entity")
public class Member {

    @Id
    @Schema(hidden = true)
    private String id;

    @Size(min = 1, max = 40)
    @NotNull
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    @Schema(description = "Name of the member", example = "Akhil")
    private String name;

    @NotEmpty
    @NotNull
    @Email
    @Indexed(unique = true)
    @Schema(description = "Email address of the member", example = "bhatia@gmail.com")
    private String email;

    @Size(min = 10, max = 12)
    @NotNull
    @Pattern(regexp = "\\d{10,12}", message = "Phone number must be between 10 and 12 digits")
    @Schema(description = "Phone number of the member", example = "9977882211")
    private String phoneNumber;
}
