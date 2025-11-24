package project.dorce.datasetmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @JoinColumn(name = "raw_dataset_id", nullable = false)
    @JsonIgnore
    private RawDataset rawDataset;

    @Column(nullable = false)
    private String storageReference;

    @Column
    private String batchName;

    @Column
    private Long sizeInBytes;

    @Column
    private Integer entryCount;

    @CreationTimestamp
    private LocalDateTime appendedAt;
}