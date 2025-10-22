package project.dorce.datasetmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class DatasetCreationRequest {
    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String description;
}
