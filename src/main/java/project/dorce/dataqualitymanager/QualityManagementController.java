package project.dorce.dataqualitymanager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.raw.RawDataAvailabilityResponse;
import project.dorce.dataqualitymanager.dto.SetQualityTagRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/api/quality")
@Tag(name = "Quality Management", description = "Endpoints for data quality management")
public class QualityManagementController {

    private final QualityManagementService qualityManagementService;

    public QualityManagementController(QualityManagementService qualityManagementService) {
        this.qualityManagementService = qualityManagementService;
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/entries/{entryId}/mark-suspicious")
    @Operation(summary = "Mark data entry as suspicious")
    public ResponseEntity<?> markDataEntrySuspicious(@PathVariable UUID entryId) {
        try {
            qualityManagementService.markDataEntrySuspicious(entryId);
            return ResponseEntity.ok("Data entry marked as suspicious");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping("/entries/{entryId}/mark-erroneous")
    @Operation(summary = "Mark data entry as erroneous")
    public ResponseEntity<?> markDataEntryErroneous(@PathVariable UUID entryId) {
        try {
            qualityManagementService.markDataEntryErroneous(entryId);
            return ResponseEntity.ok("Data entry marked as erroneous");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping("/datasets/{datasetId}/raw-download-link")
    @Operation(summary = "Get raw data download link")
    public ResponseEntity<?> getRawDownloadLink(@PathVariable UUID datasetId) {
        try {
            String downloadLink = qualityManagementService.getRawDownloadLink(datasetId);
            return ResponseEntity.ok(downloadLink);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/datasets/{datasetId}/raw-availability")
    @Operation(summary = "Check raw data availability")
    public ResponseEntity<?> checkRawAvailability(@PathVariable UUID datasetId) {
        try {
            RawDataAvailabilityResponse response = qualityManagementService.checkRawAvailability(datasetId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PutMapping("/datasets/{datasetId}/quality-tag")
    @Operation(summary = "Set quality tag for dataset")
    public ResponseEntity<?> setQualityTag(
            @PathVariable UUID datasetId,
            @Valid @RequestBody SetQualityTagRequest request
    ) {
        try {
            qualityManagementService.setQualityTag(datasetId, request.getQualityTag());
            return ResponseEntity.ok("Quality tag set successfully");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping("/alerts")
    @Operation(summary = "List quality validity alerts")
    public ResponseEntity<?> listQualityValidityAlerts(
            @RequestParam(required = false) UUID datasetId,
            @RequestParam(required = false) Boolean unresolvedOnly
    ) {
        try {
            var alerts = qualityManagementService.listQualityValidityAlerts(datasetId, unresolvedOnly);
            return ResponseEntity.ok(alerts);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping("/datasets/{datasetId}/raw-batches")
    @Operation(summary = "List raw data batches")
    public ResponseEntity<?> listRawBatches(@PathVariable UUID datasetId) {
        try {
            var batches = qualityManagementService.listRawBatches(datasetId);
            return ResponseEntity.ok(batches);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}