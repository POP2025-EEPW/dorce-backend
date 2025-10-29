package project.dorce.datasetmanager.dto;

import lombok.Data;
import project.dorce.datasetmanager.DataRelatedRequest;
import project.dorce.datasetmanager.DataRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DataRelatedRequestDto {
    private UUID id;
    private UUID datasetId;
    private UUID requesterId;
    private String requesterUsername;
    private String subject;
    private String description;
    private DataRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DataRelatedRequestDto from(DataRelatedRequest request) {
        final var dto = new DataRelatedRequestDto();
        dto.setId(request.getId());
        dto.setDatasetId(request.getDataset().getId());
        dto.setRequesterId(request.getRequester().getId());
        dto.setRequesterUsername(request.getRequester().getUsername());
        dto.setSubject(request.getSubject());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}
