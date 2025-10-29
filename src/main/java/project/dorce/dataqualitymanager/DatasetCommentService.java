package project.dorce.dataqualitymanager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dorce.dataqualitymanager.dto.AddDatasetCommentRequest;
import project.dorce.dataqualitymanager.dto.DatasetCommentDto;
import project.dorce.datasetmanager.DatasetService;
import project.dorce.usermanager.UserService;
import project.dorce.utils.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DatasetCommentService {

    private static final int MAX_PAGE_SIZE = 100;

    private final DatasetCommentRepository datasetCommentRepository;
    private final DatasetService datasetService;
    private final UserService userService;

    public DatasetCommentService(
            DatasetCommentRepository datasetCommentRepository,
            DatasetService datasetService,
            UserService userService
    ) {
        this.datasetCommentRepository = datasetCommentRepository;
        this.datasetService = datasetService;
        this.userService = userService;
    }

    public List<DatasetCommentDto> listDatasetComments(UUID datasetId, int page, int pageSize) {
        final var safePageSize = Math.max(1, Math.min(pageSize, MAX_PAGE_SIZE));
        final var safePage = Math.max(0, page);

        final var dataset = datasetService.getDataset(datasetId);
        final Pageable pageable = PageRequest.of(safePage, safePageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<DatasetComment> commentsPage = datasetCommentRepository.findByDatasetOrderByCreatedAtDesc(dataset, pageable);

        return commentsPage
                .getContent()
                .stream()
                .map(DatasetCommentDto::from)
                .toList();
    }

    @Transactional
    public DatasetCommentDto addDatasetComment(UUID datasetId, AddDatasetCommentRequest request) {
        final var dataset = datasetService.getDataset(datasetId);
        final var authentication = getAuthentication();

        final var username = authentication.getName();
        final var author = userService.getUserByUsername(username);
        if (author == null) {
            throw new ResourceNotFoundException("User " + username);
        }

        final var comment = new DatasetComment();
        comment.setDataset(dataset);
        comment.setAuthor(author);
        comment.setContent(request.getContent());

        final var saved = datasetCommentRepository.save(comment);
        return DatasetCommentDto.from(saved);
    }

    private Authentication getAuthentication() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Authenticated user is required");
        }
        return authentication;
    }
}
