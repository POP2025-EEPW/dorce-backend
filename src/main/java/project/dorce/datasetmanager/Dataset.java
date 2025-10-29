package project.dorce.datasetmanager;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "datasets")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean qualityControllable;

    public Dataset() {}

    public Dataset(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
