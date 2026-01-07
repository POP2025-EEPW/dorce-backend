package project.dorce.datasetmanager.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PropertyDto {
    private String name;
    private UUID typeId;
}
