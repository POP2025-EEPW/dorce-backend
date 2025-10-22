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

    @SecurityRequirement(name = "Token")
    @PostMapping
    public ResponseEntity<?> addDataset(@Valid @RequestBody DatasetCreationRequest dataset){
        try{
            datasetService.addDataset(dataset);

            return new ResponseEntity<>("Dataset added successfully", HttpStatus.CREATED);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
}
