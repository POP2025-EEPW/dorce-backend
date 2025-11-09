package project.dorce.datasetmanager.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.Data;
import project.dorce.datasetmanager.DataEntry;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DataEntryResponse {
    private UUID id;
    private UUID datasetId;
    private JsonNode content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DataEntryResponse from(DataEntry entry, ObjectMapper objectMapper) {
        final var response = new DataEntryResponse();
        response.setId(entry.getId());
        response.setDatasetId(entry.getDataset().getId());
        response.setContent(parseContent(entry.getContent(), objectMapper));
        response.setCreatedAt(entry.getCreatedAt());
        response.setUpdatedAt(entry.getUpdatedAt());
        return response;
    }

    private static JsonNode parseContent(String content, ObjectMapper objectMapper) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception ex) {
            return NullNode.getInstance();
        }
    }
}
