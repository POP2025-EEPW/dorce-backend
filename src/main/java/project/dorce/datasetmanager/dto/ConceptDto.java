package project.dorce.datasetmanager.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConceptDto {
    private String name;
    private List<PropertyDto> properties;
}
