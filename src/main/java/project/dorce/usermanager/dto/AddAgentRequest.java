package project.dorce.usermanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAgentRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type Name is required (e.g. Person, Organization)")
    private String typeName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private boolean createAsDataOwner = false;
    private boolean createAsDataSupplier = false;
}