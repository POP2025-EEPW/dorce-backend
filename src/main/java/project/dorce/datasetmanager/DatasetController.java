package project.dorce.datasetmanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.datasetmanager.dto.DatasetFilter;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {
    Integer defaultPageSize = 20;

    @Autowired
    private DatasetService datasetService;

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


    @PutMapping("/{id}")
    public ResponseEntity<?> editDataset(@PathVariable UUID id, @Valid @RequestBody DatasetEditionRequest dataset){
        try{
            datasetService.editDataset(id, dataset);
            return ResponseEntity.ok().build();
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping()
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
            @RequestParam(required = false, defaultValue = defaultPageSize.toString()) Integer pageSize
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
}
