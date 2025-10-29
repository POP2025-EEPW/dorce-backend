package project.dorce.datasetmanager.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DataEntryRequest {
    @NotNull
    private JsonNode content;
}
