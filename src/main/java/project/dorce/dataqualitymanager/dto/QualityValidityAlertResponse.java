package project.dorce.dataqualitymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityValidityAlertResponse {
    private UUID id;
    private UUID datasetId;
    private String datasetTitle;
    private String alertType;
    private String message;
    private String severity;
    private LocalDateTime createdAt;
    private Boolean resolved;
}
