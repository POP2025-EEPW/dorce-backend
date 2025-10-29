package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataEntryRepository extends JpaRepository<DataEntry, UUID> {
    Optional<DataEntry> findByIdAndDataset(UUID id, Dataset dataset);
}
