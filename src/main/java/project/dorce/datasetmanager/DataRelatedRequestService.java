package project.dorce.datasetmanager;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.datasetmanager.dto.DataRelatedRequestDto;
import project.dorce.datasetmanager.dto.SubmitDataRelatedRequest;
import project.dorce.usermanager.UserService;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DataRelatedRequestService {

    private static final int MAX_PAGE_SIZE = 100;

    private final DataRelatedRequestRepository repository;
    private final DatasetService datasetService;
    private final UserService userService;

    public DataRelatedRequestService(
            DataRelatedRequestRepository repository,
            DatasetService datasetService,
            UserService userService
    ) {
        this.repository = repository;
        this.datasetService = datasetService;
        this.userService = userService;
    }

    public List<DataRelatedRequestDto> listRequests(UUID datasetId, int page, int pageSize) {
        final var safePage = Math.max(0, page);
        final var safePageSize = Math.max(1, Math.min(pageSize, MAX_PAGE_SIZE));

        final var dataset = datasetService.getDataset(datasetId);
        final Pageable pageable = PageRequest.of(safePage, safePageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        return repository.findByDataset(dataset, pageable)
                .getContent()
                .stream()
                .map(DataRelatedRequestDto::from)
                .toList();
    }

    @Transactional
    public DataRelatedRequestDto submitRequest(UUID datasetId, SubmitDataRelatedRequest request) {
        final var dataset = datasetService.getDataset(datasetId);
        final var authentication = getAuthentication();

        final var username = authentication.getName();
        final var requester = userService.getUserByUsername(username);
        if (requester == null) {
            throw new ResourceNotFoundException("User " + username);
        }

        final var dataRequest = new DataRelatedRequest();
        dataRequest.setDataset(dataset);
        dataRequest.setRequester(requester);
        dataRequest.setSubject(request.getSubject());
        dataRequest.setDescription(request.getDescription());
        dataRequest.setStatus(DataRequestStatus.PENDING);

        return DataRelatedRequestDto.from(repository.save(dataRequest));
    }

    private Authentication getAuthentication() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Authenticated user is required");
        }
        return authentication;
    }
}
