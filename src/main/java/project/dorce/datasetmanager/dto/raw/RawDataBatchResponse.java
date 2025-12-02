package project.dorce.datasetmanager.dto.raw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawDataBatchResponse {
    private UUID id;
    private String batchName;
    private String storageReference;
    private String dataUrl;
    private Long sizeInBytes;
    private LocalDateTime uploadedAt;
}