package project.dorce.dataqualitymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RawDataAvailabilityResponse {
    private Boolean available;
    private String rawDataUrl;
}
