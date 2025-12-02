package project.dorce.datasetmanager.dto.raw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawDataAvailabilityResponse {
    private Boolean available;
    private String rawDataUrl;
}