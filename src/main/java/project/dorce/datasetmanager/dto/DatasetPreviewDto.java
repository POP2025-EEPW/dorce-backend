package project.dorce.datasetmanager.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DatasetPreviewDto {
    private UUID datasetId;
    private String title;
    private String description;
    private long entryCount;
    private List<DataEntryPreviewDto> sampleEntries;
}
