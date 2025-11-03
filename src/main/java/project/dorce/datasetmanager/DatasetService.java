package project.dorce.datasetmanager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.datasetmanager.dto.DatasetEditionRequest;
import project.dorce.datasetmanager.dto.DatasetFilter;
import project.dorce.datasetmanager.dto.DatasetSummary;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DatasetService {
    Integer maxPageSize = 200;
    Integer defaultPageSize = 20;

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

    public Dataset addDataset(DatasetCreationRequest dataset){
        var newDataset = new Dataset(dataset.getTitle(), dataset.getDescription(), dataset.getQualityControllable());
        return datasetRepository.save(newDataset);
    }

    public List<Dataset> getQualityControllableDatasets(){
        return datasetRepository.findByQualityControllableTrue();
    }
}
