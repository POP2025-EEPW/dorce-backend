package project.dorce.dataqualitymanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dorce.datasetmanager.Dataset;

import java.util.List;
import java.util.UUID;

@Repository
public interface QualityValidityAlertRepository extends JpaRepository<QualityValidityAlert, UUID> {
    List<QualityValidityAlert> findByDataset(Dataset dataset);
    List<QualityValidityAlert> findByResolvedFalse();
    List<QualityValidityAlert> findByDatasetAndResolvedFalse(Dataset dataset);
}
