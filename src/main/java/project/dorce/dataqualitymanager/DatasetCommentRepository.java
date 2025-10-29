package project.dorce.dataqualitymanager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.dorce.datasetmanager.Dataset;

import java.util.UUID;

public interface DatasetCommentRepository extends JpaRepository<DatasetComment, UUID> {
    Page<DatasetComment> findByDatasetOrderByCreatedAtDesc(Dataset dataset, Pageable pageable);
}
