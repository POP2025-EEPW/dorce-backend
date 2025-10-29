package project.dorce.datasetmanager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@Service
public class DatasetService {
    private DatasetRepository datasetRepository;

    @Autowired
    private UserService userService;

    public DatasetService(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public Dataset getDataset(UUID id) {
        return datasetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dataset not found."));
    }

    public Dataset addDataset(DatasetCreationRequest dataset, AuthToken authToken){
        var newDataset = new Dataset(dataset.getTitle(), dataset.getDescription(), userService.getUserByAuthToken(authToken.getToken()));
        return datasetRepository.save(newDataset);
    }

    public List<DatasetSummary> listOwnedDatasets(UUID userId) {
        var user = userService.getUserById(userId);
        var datasets = datasetRepository.findByOwner(user);
        return datasets.stream()
                .map(dataset -> new DatasetSummary(dataset.getId(), dataset.getTitle(), dataset.getDescription()))
                .collect(Collectors.toList());
    }
}
