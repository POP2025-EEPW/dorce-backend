package project.dorce.usermanager;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSupplierRepository extends JpaRepository<DataSupplier, UUID> {
}