package yourssu.blog.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.comment.controller.dto.CommentCreateRequest;
import yourssu.blog.domain.comment.controller.dto.CommentCreateResponse;
import yourssu.blog.domain.comment.service.port.CommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentCreateResponse createComment(Long articleId, CommentCreateRequest commentCreateRequest){
        return null;
    }
}
