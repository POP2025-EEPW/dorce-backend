package project.dorce.datasetmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddDatasetCommentRequest {
    @NotBlank
    private String content;
}
