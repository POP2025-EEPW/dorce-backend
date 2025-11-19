package project.dorce.dataqualitymanager.dto;

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
    private String dataUrl;
    private Long sizeInBytes;
    private LocalDateTime uploadedAt;
}

