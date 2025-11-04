package project.dorce.dataqualitymanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import java.util.UUID;

@Data
public class AddDatasetCommentRequest {
    @NonNull
    @NotBlank
    private String content;

    @NonNull
    private UUID datasetId;
}
