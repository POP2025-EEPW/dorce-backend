package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchemaRepository extends JpaRepository<Schema, UUID> {
    Schema findByIdEquals(UUID id);
}
