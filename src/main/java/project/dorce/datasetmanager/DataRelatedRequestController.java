package project.dorce.datasetmanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.SubmitDataRelatedRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/api/datasets/{datasetId}/requests")
public class DataRelatedRequestController {

    private final DataRelatedRequestService service;

    public DataRelatedRequestController(DataRelatedRequestService service) {
        this.service = service;
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping
    public ResponseEntity<?> submitDataRelatedRequest(
            @PathVariable UUID datasetId,
            @Valid @RequestBody SubmitDataRelatedRequest request
    ) {
        try {
            final var result = service.submitRequest(datasetId, request);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping
    public ResponseEntity<?> listDataRelatedRequests(
            @PathVariable UUID datasetId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            final var requests = service.listRequests(datasetId, page, pageSize);
            return ResponseEntity.ok(requests);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
