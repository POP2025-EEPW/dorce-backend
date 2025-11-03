package project.dorce.datasetmanager;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

import project.dorce.usermanager.User;

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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private Boolean qualityControllable;

    public Dataset() {}

    public Dataset(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }
}
