package project.dorce.datasetmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddDatasetCommentRequest {
    @NonNull
    @NotBlank
    private String content;

    @NonNull
    private UUID datasetId;
}
