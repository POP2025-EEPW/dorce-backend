package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.dorce.usermanager.User;

import java.util.UUID;
import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, UUID> {
	Page<Dataset> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Dataset> findByOwner(User owner);
    List<Dataset> findByQualityControllableTrue();
}
