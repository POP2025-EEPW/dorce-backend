package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface RawDatasetRepository extends JpaRepository<RawDataset, UUID> {
    Optional<RawDataset> findByDatasetId(UUID datasetId);
}