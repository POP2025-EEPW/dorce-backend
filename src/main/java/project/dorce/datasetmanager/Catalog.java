package project.dorce.datasetmanager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dorce.usermanager.Agent;

@Data
@Entity
@Table(name = "catalogs")
@NoArgsConstructor
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String title;

    @CreationTimestamp
    private LocalDateTime issued;

    @UpdateTimestamp
    private LocalDateTime modified;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JsonBackReference
    private Catalog parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Catalog> catalogs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Dataset> datasets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_agent_id")
    private Agent publisherAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_agent_id")
    private Agent creatorAgent;
}