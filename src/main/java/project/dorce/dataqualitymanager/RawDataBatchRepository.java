package project.dorce.dataqualitymanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dorce.datasetmanager.Dataset;

import java.util.List;
import java.util.UUID;

@Repository
public interface RawDataBatchRepository extends JpaRepository<RawDataBatch, UUID> {
    List<RawDataBatch> findByDataset(Dataset dataset);
}
