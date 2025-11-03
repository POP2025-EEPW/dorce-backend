package project.dorce.dataqualitymanager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.dto.DatasetCreationRequest;
import project.dorce.utils.ResourceNotFoundException;
import project.dorce.datasetmanager.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import project.dorce.dataqualitymanager.AddDatasetCommentRequest;

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
