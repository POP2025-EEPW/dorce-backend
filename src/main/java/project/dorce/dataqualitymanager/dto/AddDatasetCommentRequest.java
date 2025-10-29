package project.dorce.dataqualitymanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddDatasetCommentRequest {
    @NotBlank
    private String content;
}
