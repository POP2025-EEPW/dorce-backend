package project.dorce.datasetmanager;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Dataset> datasets;
}
