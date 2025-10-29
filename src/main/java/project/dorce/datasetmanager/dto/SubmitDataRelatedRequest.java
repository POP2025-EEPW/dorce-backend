package project.dorce.datasetmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitDataRelatedRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String description;
}
