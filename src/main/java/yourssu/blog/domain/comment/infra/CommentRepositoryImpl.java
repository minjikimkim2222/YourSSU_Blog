package yourssu.blog.domain.comment.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yourssu.blog.domain.comment.converter.CommentConverter;
import yourssu.blog.domain.comment.jpa.CommentEntity;
import yourssu.blog.domain.comment.jpa.CommentJpaRepository;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;
    @Override
    public Comment save(Comment comment) {
        CommentEntity commentEntity = commentJpaRepository.save(CommentConverter.toEntity(comment));

        return CommentConverter.toComment(commentEntity);
    }
    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id)
                .map(commentEntity -> CommentConverter.toComment(commentEntity));
    }
    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }
}
