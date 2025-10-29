package project.dorce.datasetmanager.dto;

import lombok.Data;
import project.dorce.datasetmanager.DataEntry;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DataEntryPreviewDto {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DataEntryPreviewDto from(DataEntry dataEntry) {
        final var dto = new DataEntryPreviewDto();
        dto.setId(dataEntry.getId());
        dto.setContent(dataEntry.getContent());
        dto.setCreatedAt(dataEntry.getCreatedAt());
        dto.setUpdatedAt(dataEntry.getUpdatedAt());
        return dto;
    }
}
