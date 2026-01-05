package project.dorce.datasetmanager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.ConceptDto;
import project.dorce.datasetmanager.dto.PropertyDto;
import project.dorce.datasetmanager.dto.SchemaDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SchemaService {
    private final SchemaRepository schemaRepository;
    private final TypeRepository typeRepository;

    public SchemaService(SchemaRepository schemaRepository, TypeRepository typeRepository) {
        this.schemaRepository = schemaRepository;
        this.typeRepository = typeRepository;
    }

    public List<Schema> getAllSchemas() {
        return schemaRepository.findAll();
    }

    public Schema getSchemaById(UUID id) {
        return schemaRepository.findById(id).orElseThrow();
    }

    public Schema createSchema(SchemaDto dto) {
        Schema schema = new Schema();
        schema.setTitle(dto.getTitle());
        schema.setDescription(dto.getDescription());

        // 1. Process Concepts
        if (dto.getConcepts() != null) {
            List<Concept> concepts = new ArrayList<>();

            for (ConceptDto cDto : dto.getConcepts()) {
                Concept concept = new Concept();
                concept.setName(cDto.getName());
                concept.setSchema(schema);

                if (cDto.getProperties() != null) {
                    List<Property> properties = new ArrayList<>();
                    for (PropertyDto pDto : cDto.getProperties()) {
                        Property property = new Property();
                        property.setName(pDto.getName());
                        property.setConcept(concept);

                        Type type = typeRepository.findById(pDto.getTypeId())
                                .orElseThrow(() -> new RuntimeException("Type not found"));
                        property.setType(type);

                        properties.add(property);
                    }
                    concept.setProperties(properties);
                }
                concepts.add(concept);
            }
            schema.setConcepts(concepts);
        }

        if (dto.getConstraints() != null) {
            List<Constraint> constraints = dto.getConstraints().stream().map(cDto -> {
                Constraint c = new Constraint();
                c.setRule(cDto.getRule());
                c.setSchema(schema);
                return c;
            }).toList();
            schema.setConstraints(constraints);
        }

        return schemaRepository.save(schema);
    }
}
