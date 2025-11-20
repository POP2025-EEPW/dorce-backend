package project.dorce.dataqualitymanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetQualityTagRequest {
    @NotBlank(message = "Quality tag cannot be blank")
    private String qualityTag;
}

