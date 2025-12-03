package project.dorce.datasetmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.DataEntryRequest;
import project.dorce.datasetmanager.dto.DataEntryResponse;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/api/datasets/{datasetId}/entries")
public class DataEntryController {

    private final DataEntryService dataEntryService;
    private final ObjectMapper objectMapper;

    public DataEntryController(DataEntryService dataEntryService, ObjectMapper objectMapper) {
        this.dataEntryService = dataEntryService;
        this.objectMapper = objectMapper;
    }

    @SecurityRequirement(name = "AuthToken")
    @PostMapping
    public ResponseEntity<?> addDataEntry(
            @PathVariable UUID datasetId,
            @Valid @RequestBody DataEntryRequest request
    ) {
        try {
            final var entry = dataEntryService.addDataEntry(datasetId, request);
            final var response = DataEntryResponse.from(entry, objectMapper);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @PutMapping("/{entryId}")
    public ResponseEntity<?> editDataEntry(
            @PathVariable UUID datasetId,
            @PathVariable UUID entryId,
            @Valid @RequestBody DataEntryRequest request
    ) {
        try {
            final var entry = dataEntryService.editDataEntry(datasetId, entryId, request);
            final var response = DataEntryResponse.from(entry, objectMapper);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @SecurityRequirement(name = "AuthToken")
    @GetMapping
    public ResponseEntity<?> getAllDataEntries(@PathVariable UUID datasetId) {
        try {
            final var entries = dataEntryService.getAllDataEntries(datasetId);
            final var responses = entries.stream()
                    .map(entry -> DataEntryResponse.from(entry, objectMapper))
                    .toList();
            return ResponseEntity.ok(responses);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
