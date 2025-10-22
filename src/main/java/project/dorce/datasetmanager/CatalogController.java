package project.dorce.datasetmanager;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.AddCatalogRequest;
import project.dorce.usermanager.UserService;
import project.dorce.usermanager.dto.UserRegistrationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {
    @Autowired
    CatalogService catalogService;

    @Autowired
    DatasetService datasetService;

    @PostMapping
    public ResponseEntity<?> addCatalog(@Valid @RequestBody AddCatalogRequest request){
        final var catalogDto = request.getCatalog();

        var catalog = new Catalog();
        catalog.setDescription(catalogDto.getDescription());
        catalog.setTitle(catalogDto.getTitle());

        List<Dataset> datasets = new ArrayList<>();
        for (final var datasetId : catalogDto.getDatasets()) {
            datasets.add(datasetService.getDataset(datasetId));
        }
        catalog.setDatasets(datasets);

        catalog = catalogService.addCatalog(request.getParentCatalogId(), catalog);
        return new ResponseEntity<>(catalog, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> listCatalogs(@Valid @RequestParam(required = false) UUID parentCatalogId) {
        try {
            final var catalogs = catalogService.listCatalogs(parentCatalogId);
            return new ResponseEntity<>(catalogs, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<?> getCatalog(@Valid @PathVariable UUID catalogId) {
        final var catalog = catalogService.getCatalog(catalogId);
        if (catalog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(catalog, HttpStatus.OK);
    }

    @GetMapping("/{catalogId}/datasets")
    public ResponseEntity<?> getCatalogDatasets(
            @Valid @PathVariable UUID catalogId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        final var datasets = catalogService.listCatalogDatasets(catalogId, page, pageSize);
        return new ResponseEntity<>(datasets, HttpStatus.OK);
    }
}
