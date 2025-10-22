package project.dorce.datasetmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dorce.usermanager.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogRepository extends JpaRepository<Catalog, UUID> {
    List<Catalog> findAllByParentIsNull();

    List<Catalog> findAllByParent(Catalog parent);
}
