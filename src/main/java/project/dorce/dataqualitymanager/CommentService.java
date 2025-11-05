package project.dorce.dataqualitymanager;

import org.springframework.stereotype.Service;
import project.dorce.datasetmanager.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import project.dorce.dataqualitymanager.dto.AddDatasetCommentRequest;


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
