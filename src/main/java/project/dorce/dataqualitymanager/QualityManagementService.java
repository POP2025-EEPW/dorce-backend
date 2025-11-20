package project.dorce.dataqualitymanager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.dataqualitymanager.dto.QualityValidityAlertResponse;
import project.dorce.dataqualitymanager.dto.RawDataAvailabilityResponse;
import project.dorce.dataqualitymanager.dto.RawDataBatchResponse;
import project.dorce.datasetmanager.DataEntryRepository;
import project.dorce.datasetmanager.DatasetService;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class QualityManagementService {

    private final DataEntryRepository dataEntryRepository;
    private final DatasetService datasetService;
    private final QualityValidityAlertRepository alertRepository;
    private final RawDataBatchRepository rawDataBatchRepository;

    public QualityManagementService(
            DataEntryRepository dataEntryRepository,
            DatasetService datasetService,
            QualityValidityAlertRepository alertRepository,
            RawDataBatchRepository rawDataBatchRepository
    ) {
        this.dataEntryRepository = dataEntryRepository;
        this.datasetService = datasetService;
        this.alertRepository = alertRepository;
        this.rawDataBatchRepository = rawDataBatchRepository;
    }

    public void markDataEntrySuspicious(UUID entryId) {
        var entry = dataEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("DataEntry not found"));

        entry.setSuspicious(true);
        dataEntryRepository.save(entry);

        // Create an alert for this suspicious entry
        var alert = new QualityValidityAlert(
                entry.getDataset(),
                "SUSPICIOUS_ENTRY",
                "Data entry " + entryId + " has been marked as suspicious",
                "MEDIUM"
        );
        alertRepository.save(alert);
    }

    public void markDataEntryErroneous(UUID entryId) {
        var entry = dataEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("DataEntry not found"));

        entry.setErroneous(true);
        dataEntryRepository.save(entry);

        // Create an alert for this erroneous entry
        var alert = new QualityValidityAlert(
                entry.getDataset(),
                "ERRONEOUS_ENTRY",
                "Data entry " + entryId + " has been marked as erroneous",
                "HIGH"
        );
        alertRepository.save(alert);
    }

    public String getRawDownloadLink(UUID datasetId) {
        var dataset = datasetService.getDataset(datasetId);

        if (!dataset.getRawDataAvailable()) {
            throw new IllegalStateException("Raw data is not available for this dataset");
        }

        if (dataset.getRawDataUrl() == null || dataset.getRawDataUrl().isEmpty()) {
            throw new IllegalStateException("Raw data URL is not configured");
        }

        return dataset.getRawDataUrl();
    }

    public RawDataAvailabilityResponse checkRawAvailability(UUID datasetId) {
        var dataset = datasetService.getDataset(datasetId);
        return new RawDataAvailabilityResponse(
                dataset.getRawDataAvailable(),
                dataset.getRawDataUrl()
        );
    }

    public void setQualityTag(UUID datasetId, String qualityTag) {
        var dataset = datasetService.getDataset(datasetId);
        dataset.setQualityTag(qualityTag);

        // Create an alert when quality tag is set
        var alert = new QualityValidityAlert(
                dataset,
                "QUALITY_TAG_SET",
                "Quality tag set to: " + qualityTag,
                "LOW"
        );
        alertRepository.save(alert);
    }

    public List<QualityValidityAlertResponse> listQualityValidityAlerts(UUID datasetId, Boolean resolvedOnly) {
        List<QualityValidityAlert> alerts;

        if (datasetId != null) {
            var dataset = datasetService.getDataset(datasetId);
            if (resolvedOnly != null && !resolvedOnly) {
                alerts = alertRepository.findByDatasetAndResolvedFalse(dataset);
            } else {
                alerts = alertRepository.findByDataset(dataset);
            }
        } else {
            if (resolvedOnly != null && !resolvedOnly) {
                alerts = alertRepository.findByResolvedFalse();
            } else {
                alerts = alertRepository.findAll();
            }
        }

        return alerts.stream()
                .map(alert -> new QualityValidityAlertResponse(
                        alert.getId(),
                        alert.getDataset().getId(),
                        alert.getDataset().getTitle(),
                        alert.getAlertType(),
                        alert.getMessage(),
                        alert.getSeverity(),
                        alert.getCreatedAt(),
                        alert.getResolved()
                ))
                .collect(Collectors.toList());
    }

    public List<RawDataBatchResponse> listRawBatches(UUID datasetId) {
        var dataset = datasetService.getDataset(datasetId);
        var batches = rawDataBatchRepository.findByDataset(dataset);

        return batches.stream()
                .map(batch -> new RawDataBatchResponse(
                        batch.getId(),
                        batch.getBatchName(),
                        batch.getDataUrl(),
                        batch.getSizeInBytes(),
                        batch.getUploadedAt()
                ))
                .collect(Collectors.toList());
    }

    public void registerRawDataset(UUID datasetId, String rawDataUrl) {
        var dataset = datasetService.getDataset(datasetId);
        dataset.setRawDataUrl(rawDataUrl);
        dataset.setRawDataAvailable(true);
    }

    public RawDataBatchResponse appendRawBatch(UUID datasetId, String batchName, String dataUrl, Long sizeInBytes) {
        var dataset = datasetService.getDataset(datasetId);

        var batch = new RawDataBatch(dataset, batchName, dataUrl, sizeInBytes);
        var savedBatch = rawDataBatchRepository.save(batch);

        // Update dataset raw data availability
        dataset.setRawDataAvailable(true);

        return new RawDataBatchResponse(
                savedBatch.getId(),
                savedBatch.getBatchName(),
                savedBatch.getDataUrl(),
                savedBatch.getSizeInBytes(),
                savedBatch.getUploadedAt()
        );
    }
}

