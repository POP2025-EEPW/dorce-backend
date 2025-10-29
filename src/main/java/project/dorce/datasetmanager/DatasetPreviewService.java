package project.dorce.datasetmanager;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DataEntryPreviewDto;
import project.dorce.datasetmanager.dto.DatasetPreviewDto;

import java.util.UUID;

@Service
public class DatasetPreviewService {

    private static final int MAX_SAMPLE_SIZE = 50;

    private final DatasetService datasetService;
    private final DataEntryRepository dataEntryRepository;

    public DatasetPreviewService(DatasetService datasetService, DataEntryRepository dataEntryRepository) {
        this.datasetService = datasetService;
        this.dataEntryRepository = dataEntryRepository;
    }

    public DatasetPreviewDto getDatasetPreview(UUID datasetId, int sampleSize) {
        if (sampleSize <= 0) {
            throw new IllegalArgumentException("sampleSize must be greater than zero");
        }

        final var safeSampleSize = Math.min(sampleSize, MAX_SAMPLE_SIZE);
        final var dataset = datasetService.getDataset(datasetId);

        final var pageable = PageRequest.of(0, safeSampleSize, Sort.by(Sort.Direction.ASC, "createdAt"));
        final var sampleEntries = dataEntryRepository
                .findByDataset(dataset, pageable)
                .getContent()
                .stream()
                .map(DataEntryPreviewDto::from)
                .toList();

        final var preview = new DatasetPreviewDto();
        preview.setDatasetId(dataset.getId());
        preview.setTitle(dataset.getTitle());
        preview.setDescription(dataset.getDescription());
        preview.setEntryCount(dataEntryRepository.countByDataset(dataset));
        preview.setSampleEntries(sampleEntries);

        return preview;
    }
}
