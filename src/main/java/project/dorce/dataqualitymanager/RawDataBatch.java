package project.dorce.dataqualitymanager;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import project.dorce.datasetmanager.Dataset;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "raw_data_batches")
@NoArgsConstructor
public class RawDataBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;

    @Column(nullable = false)
    private String batchName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dataUrl;

    @Column
    private Long sizeInBytes;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    public RawDataBatch(Dataset dataset, String batchName, String dataUrl, Long sizeInBytes) {
        this.dataset = dataset;
        this.batchName = batchName;
        this.dataUrl = dataUrl;
        this.sizeInBytes = sizeInBytes;
    }
}
