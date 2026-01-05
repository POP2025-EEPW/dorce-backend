package project.dorce.datasetmanager.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SchemaDto {
    private UUID id;
    private String title;
    private String description;
    private List<ConceptDto> concepts;
    private List<ConstraintDto> constraints;
}
