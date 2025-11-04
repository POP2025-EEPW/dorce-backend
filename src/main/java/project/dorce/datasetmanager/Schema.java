package project.dorce.datasetmanager;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "schemas")
public class Schema {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Schema() {}
}
