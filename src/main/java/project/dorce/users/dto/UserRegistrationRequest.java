package project.dorce.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegistrationRequest {
    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String password;
}
