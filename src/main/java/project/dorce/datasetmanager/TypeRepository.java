package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
    Optional<Type> findByName(String name);
}
