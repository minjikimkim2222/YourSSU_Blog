package yourssu.blog.domain.comment.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yourssu.blog.domain.comment.converter.CommentConverter;
import yourssu.blog.domain.comment.jpa.CommentEntity;
import yourssu.blog.domain.comment.jpa.CommentJpaRepository;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        return commentJpaRepository.findByArticleEntity_Id(articleId)
                .stream().map(commentEntity -> CommentConverter.toComment(commentEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(CommentConverter.toEntity(comment));
    }
    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentJpaRepository.findByUserEntity_Id(userId)
                .stream().map(commentEntity -> CommentConverter.toComment(commentEntity))
                .collect(Collectors.toList());
    }
}
