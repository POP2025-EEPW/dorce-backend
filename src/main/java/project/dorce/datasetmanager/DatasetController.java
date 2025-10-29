package project.dorce.datasetmanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private DatasetPreviewService datasetPreviewService;

    @SecurityRequirement(name = "AuthToken")
    @PostMapping
    public ResponseEntity<?> addDataset(@Valid @RequestBody DatasetCreationRequest dataset){
            final var result = datasetService.addDataset(dataset);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDataset(@PathVariable UUID id){
        try{
            return ResponseEntity.ok(datasetService.getDataset(id));
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<?> getDatasetPreview(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "5") Integer sampleSize
    ) {
        try {
            final var preview = datasetPreviewService.getDatasetPreview(id, sampleSize);
            return ResponseEntity.ok(preview);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
