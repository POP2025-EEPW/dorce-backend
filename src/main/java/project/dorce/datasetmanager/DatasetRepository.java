package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DatasetRepository extends JpaRepository<Dataset, UUID> {
    
}
