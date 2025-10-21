package project.dorce.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthRequest {
    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String password;
}
