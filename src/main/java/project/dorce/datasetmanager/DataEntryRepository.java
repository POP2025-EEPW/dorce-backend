package project.dorce.datasetmanager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataEntryRepository extends JpaRepository<DataEntry, UUID> {
    Optional<DataEntry> findByIdAndDataset(UUID id, Dataset dataset);
    Page<DataEntry> findByDataset(Dataset dataset, Pageable pageable);
    long countByDataset(Dataset dataset);
    List<DataEntry> findAllByDataset(Dataset dataset);
}
