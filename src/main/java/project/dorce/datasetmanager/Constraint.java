package project.dorce.datasetmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "constraints")
public class Constraint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String rule;

    @ManyToOne
    @JsonIgnore
    private Schema schema;
}
