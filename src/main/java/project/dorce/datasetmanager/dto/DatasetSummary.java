package project.dorce.datasetmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetSummary {
    @NonNull
    @NotBlank
    private UUID id;

    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String description;
}
