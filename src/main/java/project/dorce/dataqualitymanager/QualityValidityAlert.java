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
@Table(name = "quality_validity_alerts")
@NoArgsConstructor
public class QualityValidityAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;

    @Column(nullable = false)
    private String alertType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String severity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean resolved = false;

    public QualityValidityAlert(Dataset dataset, String alertType, String message, String severity) {
        this.dataset = dataset;
        this.alertType = alertType;
        this.message = message;
        this.severity = severity;
    }
}
