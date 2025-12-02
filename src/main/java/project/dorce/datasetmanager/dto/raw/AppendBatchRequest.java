package project.dorce.datasetmanager.dto.raw;

import lombok.Data;

@Data
public class AppendBatchRequest {
    private String storageReference;
    private String batchName;
    private String dataUrl;
    private Long sizeInBytes;
    private Integer entryCount;
}