package project.dorce.datasetmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "raw_datasets")
@NoArgsConstructor
public class RawDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = false, unique = true)
    @JsonIgnore
    private Dataset dataset;

    @Column
    private String resourceUri;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "rawDataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RawDataBatch> batches;
}