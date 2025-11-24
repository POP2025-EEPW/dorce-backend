package project.dorce.datasetmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.datasetmanager.dto.DatasetEditionRequest;
import project.dorce.datasetmanager.dto.DatasetFilter;
import project.dorce.datasetmanager.dto.DatasetSummary;
import project.dorce.datasetmanager.dto.raw.AppendBatchRequest;
import project.dorce.datasetmanager.dto.raw.RegisterRawDatasetRequest;
import project.dorce.usermanager.UserService;
import project.dorce.utils.InvalidDatasetStatusException;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DatasetService {
    Integer maxPageSize = 200;
    Integer defaultPageSize = 20;

    private DatasetRepository datasetRepository;
    private SchemaRepository schemaRepository;

    @Autowired
    private RawDatasetRepository rawDatasetRepository;
    @Autowired
    private RawDataBatchRepository rawDataBatchRepository;

    @Autowired
    private UserService userService;

    public DatasetService(DatasetRepository datasetRepository, SchemaRepository schemaRepository) {
        this.datasetRepository = datasetRepository;
        this.schemaRepository = schemaRepository;
    }

    public Dataset getDataset(UUID id) {
        return datasetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dataset not found."));
    }

    public Dataset addDataset(DatasetCreationRequest dataset, String authToken){
        var newDataset = new Dataset(
                dataset.getTitle(),
                dataset.getDescription(),
                userService.getUserByAuthToken(authToken),
                schemaRepository.findById(dataset.getSchemaId()).orElseThrow(() -> new ResourceNotFoundException("Dataset not found.")),
                dataset.getQualityControllable()
        );
        return datasetRepository.save(newDataset);
    }

    public void editDataset(UUID id, DatasetEditionRequest dataset) {
        var existingDataset = getDataset(id);
        existingDataset.setTitle(dataset.getTitle());
        existingDataset.setDescription(dataset.getDescription());
        datasetRepository.save(existingDataset);
    }

    public List<DatasetSummary> listDatasets(DatasetFilter filter, Integer page, Integer pageSize){
        int p = (page == null || page < 0) ? 0 : page;
        int ps = (pageSize == null || pageSize <= 0 || pageSize > maxPageSize) ? defaultPageSize : pageSize;
        Pageable pageable = PageRequest.of(p, ps);

        Page<Dataset> result;
        String title = filter != null ? filter.getTitle() : null;
        if (title != null && !title.isBlank()){
            result = datasetRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
        } else {
            result = datasetRepository.findAll(pageable);
        }

        return result.getContent().stream()
                .map(d -> new DatasetSummary(d.getId(), d.getTitle(), d.getDescription()))
                .collect(Collectors.toList());
    }

    public List<DatasetSummary> listOwnedDatasets(UUID userId) {
        var user = userService.getUserById(userId);
        var datasets = datasetRepository.findByOwner(user);
        return datasets.stream()
                .map(dataset -> new DatasetSummary(dataset.getId(), dataset.getTitle(), dataset.getDescription()))
                .collect(Collectors.toList());
    }

    public List<Dataset> getQualityControllableDatasets(){
        return datasetRepository.findByQualityControllableTrue();
    }

    public void setDataSchema(UUID datasetId, UUID schemaId) {
        Schema schema = schemaRepository.findByIdEquals(schemaId).orElseThrow(() -> new ResourceNotFoundException("Schema not found."));
        Dataset dataset = getDataset(datasetId);
        dataset.setSchema(schema);
        datasetRepository.save(dataset);
    }


    public Dataset submitDataset(UUID datasetId) {
        Dataset dataset = getDataset(datasetId);

        if (dataset.getStatus() != DatasetStatus.DRAFT) {
            throw new InvalidDatasetStatusException(
                    "Dataset cannot be submitted. Expected status: DRAFT, but was: " + dataset.getStatus()
            );
        }

        dataset.setStatus(DatasetStatus.SUBMITTED);
        return datasetRepository.save(dataset);
    }

    public Dataset publishDataset(UUID datasetId) {
        Dataset dataset = getDataset(datasetId);

        if (dataset.getStatus() != DatasetStatus.SUBMITTED) {
            throw new InvalidDatasetStatusException(
                    "Dataset cannot be published. Expected status: SUBMITTED, but was: " + dataset.getStatus()
            );
        }

        dataset.setStatus(DatasetStatus.ACTIVE);
        return datasetRepository.save(dataset);
    }

    public Dataset archiveDataset(UUID datasetId) {
        Dataset dataset = getDataset(datasetId);

        if (dataset.getStatus() == DatasetStatus.DELETED) {
            throw new InvalidDatasetStatusException(
                    "Dataset is already archived (DELETED)."
            );
        }

        dataset.setStatus(DatasetStatus.DELETED);
        return datasetRepository.save(dataset);
    }

    public RawDataset registerRawDataset(UUID datasetId, RegisterRawDatasetRequest request) {
        var dataset = getDataset(datasetId);

        if (rawDatasetRepository.findByDatasetId(datasetId).isPresent()) {
            throw new IllegalStateException("RawDataset is already registered for Dataset: " + datasetId);
        }

        RawDataset rawDataset = new RawDataset();
        rawDataset.setDataset(dataset);

        rawDataset.setResourceUri(request.getDataUrl());

        return rawDatasetRepository.save(rawDataset);
    }

    public RawDataset getRawDatasetByDatasetId(UUID datasetId) {
        return rawDatasetRepository.findByDatasetId(datasetId)
                .orElseThrow(() -> new ResourceNotFoundException("RawDataset not found for Dataset: " + datasetId));
    }

    public RawDataBatch appendRawBatch(UUID datasetId, AppendBatchRequest request) {
        var rawDataset = getRawDatasetByDatasetId(datasetId);

        RawDataBatch batch = new RawDataBatch();
        batch.setRawDataset(rawDataset);

        batch.setStorageReference(request.getStorageReference());
        batch.setBatchName(request.getBatchName() != null ? request.getBatchName() : request.getStorageReference());
        batch.setSizeInBytes(request.getSizeInBytes());
        batch.setEntryCount(request.getEntryCount());

        return rawDataBatchRepository.save(batch);
    }

    public Page<RawDataBatch> listRawBatches(UUID datasetId, Pageable pageable) {
        RawDataset rawDataset = getRawDatasetByDatasetId(datasetId);

        return rawDataBatchRepository.findByRawDatasetId(rawDataset.getId(), pageable);
    }
}