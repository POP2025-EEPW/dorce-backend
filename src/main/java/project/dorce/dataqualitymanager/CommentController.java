package project.dorce.dataqualitymanager;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.utils.ResourceNotFoundException;
import project.dorce.dataqualitymanager.dto.AddDatasetCommentRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/datasets/{datasetId}/quality")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @SecurityRequirement(name = "AuthToken")
    @PostMapping
    public ResponseEntity<?> addDatasetComment(
            @PathVariable UUID datasetId,
            @Valid @RequestBody AddDatasetCommentRequest addDatasetCommentRequest){
            final var result = commentService.addDatasetComment(datasetId, addDatasetCommentRequest);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
