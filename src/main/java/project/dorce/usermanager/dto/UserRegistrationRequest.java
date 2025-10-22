package project.dorce.usermanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import project.dorce.usermanager.Role;

import java.util.List;

@Data
public class UserRegistrationRequest {
    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String password;

    @NonNull
    private List<Role> roles;
}
