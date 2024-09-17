package yourssu.blog.domain.comment.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yourssu.blog.domain.comment.jpa.CommentJpaRepository;
import yourssu.blog.domain.comment.service.port.CommentRepository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;
}
