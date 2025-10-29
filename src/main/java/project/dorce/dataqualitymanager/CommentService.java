package project.dorce.datasetmanager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.utils.ResourceNotFoundException;

import java.util.UUID;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    @Autowired
    private DatasetService datasetService;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addDatasetComment(AddDatasetCommentRequest commentRequest){
        var newComment = new Comment(commentRequest.getContent(), datasetService.getDataset(commentRequest.getDatasetId()));
        return commentRepository.save(newComment);
    }

}
