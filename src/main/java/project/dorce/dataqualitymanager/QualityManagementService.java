package project.dorce.dataqualitymanager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.dataqualitymanager.dto.QualityValidityAlertResponse;
import project.dorce.datasetmanager.*;
import project.dorce.datasetmanager.dto.raw.RawDataAvailabilityResponse;
import project.dorce.datasetmanager.dto.raw.RawDataBatchResponse;
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
    private final RawDatasetRepository rawDatasetRepository;

    public QualityManagementService(
            DataEntryRepository dataEntryRepository,
            DatasetService datasetService,
            QualityValidityAlertRepository alertRepository,
            RawDataBatchRepository rawDataBatchRepository,
            RawDatasetRepository rawDatasetRepository
    ) {
        this.dataEntryRepository = dataEntryRepository;
        this.datasetService = datasetService;
        this.alertRepository = alertRepository;
        this.rawDataBatchRepository = rawDataBatchRepository;
        this.rawDatasetRepository = rawDatasetRepository;
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
        RawDataset rawDataset = rawDatasetRepository.findByDatasetId(datasetId)
                .orElseThrow(() -> new IllegalStateException("Raw data is not registered for this dataset"));

        if (rawDataset.getResourceUri() == null || rawDataset.getResourceUri().isEmpty()) {
            throw new IllegalStateException("Raw data URI is not configured");
        }

        return rawDataset.getResourceUri();
    }

    public RawDataAvailabilityResponse checkRawAvailability(UUID datasetId) {
        var dataset = datasetService.getDataset(datasetId);
        return new RawDataAvailabilityResponse(
                dataset.getRawDataAvailable(),
                dataset.getRawDataUrl()
        );
    }

    public List<RawDataBatchResponse> listRawBatches(UUID datasetId) {
        var rawDataset = datasetService.getRawDatasetByDatasetId(datasetId);

        var batchesPage = rawDataBatchRepository.findByRawDatasetId(
                rawDataset.getId(),
                Pageable.unpaged()
        );

        return batchesPage.stream()
                .map(batch -> new RawDataBatchResponse(
                        batch.getId(),
                        batch.getBatchName() != null ? batch.getBatchName() : batch.getStorageReference(),
                        batch.getStorageReference(),
                        batch.getStorageReference(),
                        batch.getSizeInBytes(),
                        batch.getAppendedAt()
                ))
                .collect(Collectors.toList());
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
}