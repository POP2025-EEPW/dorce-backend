package project.dorce.datasetmanager;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dorce.usermanager.Agent;
import project.dorce.usermanager.DataOwner;
import project.dorce.usermanager.DataSupplier;
import project.dorce.usermanager.User;

@Data
@Entity
@Table(name = "datasets")
@NoArgsConstructor
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_owner_id", nullable = false) 
    private User owner;

    @Column(nullable = false)
    private Boolean qualityControllable;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Schema schema;

    @Column
    private String qualityTag;

    @Column
    private String rawDataUrl;

    @Column(nullable = false)
    private Boolean rawDataAvailable = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetStatus status;

    @Column
    private LocalDate issuedDate;

    @Column
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_agent_id")
    private Agent publisherAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_point_agent_id")
    private Agent contactPointAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_supplier_id")
    private DataSupplier dataSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_owner_id")
    private DataOwner dataOwner;

    public Dataset(String title, String description, User owner, Schema schema, Boolean qualityControllable) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.schema = schema;
        this.qualityControllable = qualityControllable;
        this.status = DatasetStatus.DRAFT;
    }
}