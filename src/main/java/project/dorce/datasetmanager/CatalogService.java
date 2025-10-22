package project.dorce.datasetmanager;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dorce.utils.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public Catalog addCatalog(UUID parentCatalogId, Catalog catalog) {
        if (parentCatalogId != null) {
            final var parent = catalogRepository
                    .findById(parentCatalogId)
                    .orElseThrow(() -> new ResourceNotFoundException("Catalog " + parentCatalogId));
            catalog.setParent(parent);
        }

        catalog = catalogRepository.save(catalog);
        return catalog;
    }

    public Catalog getCatalog(UUID catalogId) {
        return catalogRepository.findById(catalogId).orElse(null);
    }

    public List<Catalog> listCatalogs(UUID parentCatalogId) {
        if (parentCatalogId == null) {
            return catalogRepository.findAllByParentIsNull();
        } else {
            final var parent = catalogRepository.findById(parentCatalogId).orElseThrow(() -> new ResourceNotFoundException("Catalog " + parentCatalogId));
            return catalogRepository.findAllByParent(parent);
        }
    }

    public List<Dataset> listCatalogDatasets(UUID catalogId, Integer page, Integer pageSize) {
        final var catalog = catalogRepository
                .findById(catalogId)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog " + catalogId));
        final var datasets = catalog.getDatasets();

        List<Dataset> filtered = new ArrayList<>();
        final var start = page * pageSize;
        final var end = (page + 1) * pageSize;
        for (int i = start; i < end && i < datasets.size(); i++) {
            filtered.add(catalog.getDatasets().get(i));
        }

        return filtered;
    }
}
