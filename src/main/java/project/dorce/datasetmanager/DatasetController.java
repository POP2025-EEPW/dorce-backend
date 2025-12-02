package project.dorce.datasetmanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.datasetmanager.dto.DatasetEditionRequest;
import project.dorce.datasetmanager.dto.DatasetFilter;
import project.dorce.datasetmanager.dto.raw.AppendBatchRequest;
import project.dorce.datasetmanager.dto.raw.RegisterRawDatasetRequest;
import project.dorce.usermanager.UserService;
import project.dorce.utils.InvalidDatasetStatusException;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
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
    public ResponseEntity<?> addDataset(
        @RequestHeader(name = "Authorization", required = false) String authorization,
        @Valid @RequestBody DatasetCreationRequest dataset){
            String token = UserService.extractToken(authorization);
            final var result = datasetService.addDataset(dataset, token);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/schema")
    public ResponseEntity<?> setDataSchema(@PathVariable UUID id, @RequestBody Schema schema){
        try{
            datasetService.setDataSchema(id, schema.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDataset(@PathVariable UUID id){
        try{
            return ResponseEntity.ok(datasetService.getDataset(id));
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editDataset(@PathVariable UUID id, @Valid @RequestBody DatasetEditionRequest dataset){
        try{
            datasetService.editDataset(id, dataset);
            return ResponseEntity.ok().build();
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/ownedby")
    public ResponseEntity<?> listOwnedDatasets(
        @RequestParam UUID userId
    ){
        try{
            return ResponseEntity.ok(datasetService.listOwnedDatasets(userId));
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> listDatasets(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize
    ){
        DatasetFilter filter = new DatasetFilter();
        filter.setTitle(title);
        List<?> result = datasetService.listDatasets(filter, page, pageSize);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping("/qualityControllable")
    public ResponseEntity<?> getQualityControllableDatasets(){
        try{
            return ResponseEntity.ok(datasetService.getQualityControllableDatasets());
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitDataset(@PathVariable UUID id) {
        try {
            var dataset = datasetService.submitDataset(id);
            return ResponseEntity.ok(dataset);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidDatasetStatusException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishDataset(@PathVariable UUID id) {
        try {
            var dataset = datasetService.publishDataset(id);
            return ResponseEntity.ok(dataset);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidDatasetStatusException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/{id}/archive")
    public ResponseEntity<?> archiveDataset(@PathVariable UUID id) {
        try {
            var dataset = datasetService.archiveDataset(id);
            return ResponseEntity.ok(dataset);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidDatasetStatusException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/{datasetId}/raw")
    public ResponseEntity<?> registerRawDataset(
            @PathVariable UUID datasetId,
            @Valid @RequestBody RegisterRawDatasetRequest request
    ) {
        try {
            var rawDataset = datasetService.registerRawDataset(datasetId, request);
            return new ResponseEntity<>(rawDataset, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{datasetId}/raw")
    public ResponseEntity<?> getRawDataset(@PathVariable UUID datasetId) {
        try {
            var rawDataset = datasetService.getRawDatasetByDatasetId(datasetId);
            return ResponseEntity.ok(rawDataset);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/{datasetId}/raw/batches")
    public ResponseEntity<?> appendRawBatch(
            @PathVariable UUID datasetId,
            @Valid @RequestBody AppendBatchRequest request
    ) {
        try {
            var batch = datasetService.appendRawBatch(datasetId, request);
            return new ResponseEntity<>(batch, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{datasetId}/raw/batches")
    public ResponseEntity<?> listRawBatches(
            @PathVariable UUID datasetId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize
    ) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);

            var batches = datasetService.listRawBatches(datasetId, pageable);
            return ResponseEntity.ok(batches);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}