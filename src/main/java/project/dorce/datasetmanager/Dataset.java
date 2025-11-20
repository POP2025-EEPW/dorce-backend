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
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Schema schema;

    @Column
    private String qualityTag;

    @Column
    private String rawDataUrl;

    @Column(nullable = false)
    private Boolean rawDataAvailable = false;

    public Dataset() {}

    public Dataset(String title, String description, User owner, Schema schema, Boolean qualityControllable) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.schema = schema;
        this.qualityControllable = qualityControllable;
    }
}
