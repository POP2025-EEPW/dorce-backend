package project.dorce.datasetmanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.AddDatasetCommentRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/api/datasets/{datasetId}/comments")
public class DatasetCommentController {

    private final DatasetCommentService datasetCommentService;

    public DatasetCommentController(DatasetCommentService datasetCommentService) {
        this.datasetCommentService = datasetCommentService;
    }

    @GetMapping
    public ResponseEntity<?> listDatasetComments(
            @PathVariable UUID datasetId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        try {
            final var comments = datasetCommentService.listDatasetComments(datasetId, page, pageSize);
            return ResponseEntity.ok(comments);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping
    public ResponseEntity<?> addDatasetComment(
            @PathVariable UUID datasetId,
            @Valid @RequestBody AddDatasetCommentRequest request
    ) {
        try {
            final var comment = datasetCommentService.addDatasetComment(datasetId, request);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
