package yourssu.blog.domain.comment.service.port;

import yourssu.blog.domain.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    void deleteById(Long id);
    List<Comment> findByArticleId(Long articleId);
    void delete(Comment comment);
    List<Comment> findByUserId(Long userId);
}
