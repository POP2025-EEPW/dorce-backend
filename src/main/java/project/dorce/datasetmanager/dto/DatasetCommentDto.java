package project.dorce.datasetmanager.dto;

import lombok.Data;
import project.dorce.datasetmanager.DatasetComment;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DatasetCommentDto {
    private UUID id;
    private UUID datasetId;
    private UUID authorId;
    private String authorUsername;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DatasetCommentDto from(DatasetComment comment) {
        final var dto = new DatasetCommentDto();
        dto.setId(comment.getId());
        dto.setDatasetId(comment.getDataset().getId());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorUsername(comment.getAuthor().getUsername());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
}
