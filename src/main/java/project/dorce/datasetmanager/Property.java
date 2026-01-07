package project.dorce.datasetmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    @JsonIgnore
    private Concept concept;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;
}