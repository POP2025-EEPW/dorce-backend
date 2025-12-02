package project.dorce.datasetmanager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RawDataBatchRepository extends JpaRepository<RawDataBatch, UUID> {
    Page<RawDataBatch> findByRawDatasetId(UUID rawDatasetId, Pageable pageable);
}