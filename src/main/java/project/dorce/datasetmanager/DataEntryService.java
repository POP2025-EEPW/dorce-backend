package project.dorce.datasetmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.datasetmanager.dto.DataEntryRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@Service
@Transactional
public class DataEntryService {

    private final DataEntryRepository dataEntryRepository;
    private final DatasetService datasetService;
    private final ObjectMapper objectMapper;

    public DataEntryService(
            DataEntryRepository dataEntryRepository,
            DatasetService datasetService,
            ObjectMapper objectMapper
    ) {
        this.dataEntryRepository = dataEntryRepository;
        this.datasetService = datasetService;
        this.objectMapper = objectMapper;
    }

    public DataEntry addDataEntry(UUID datasetId, DataEntryRequest request) {
        final var dataset = datasetService.getDataset(datasetId);
        final var content = serializeContent(request);

        final var entry = new DataEntry(dataset, content);
        return dataEntryRepository.save(entry);
    }

    public DataEntry editDataEntry(UUID datasetId, UUID entryId, DataEntryRequest request) {
        final var dataset = datasetService.getDataset(datasetId);
        final var entry = dataEntryRepository
                .findByIdAndDataset(entryId, dataset)
                .orElseThrow(() -> new ResourceNotFoundException("DataEntry " + entryId));

        final var content = serializeContent(request);
        entry.updateContent(content);
        return dataEntryRepository.save(entry);
    }

    private String serializeContent(DataEntryRequest request) {
        try {
            return objectMapper.writeValueAsString(request.getContent());
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Invalid entry content", ex);
        }
    }
}
