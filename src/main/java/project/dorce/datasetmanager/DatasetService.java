package project.dorce.datasetmanager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.datasetmanager.dto.DatasetEditionRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@Service
public class DatasetService {
    private DatasetRepository datasetRepository;

    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public Dataset getDataset(UUID id) {
        return datasetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dataset not found."));
    }

    public Dataset addDataset(DatasetCreationRequest dataset){
        var newDataset = new Dataset(dataset.getTitle(), dataset.getDescription());
        return datasetRepository.save(newDataset);
    }

    public void editDataset(UUID id, DatasetEditionRequest dataset) {
        var existingDataset = getDataset(id);
        existingDataset.setTitle(dataset.getTitle());
        existingDataset.setDescription(dataset.getDescription());
        datasetRepository.save(existingDataset);
    }

}
