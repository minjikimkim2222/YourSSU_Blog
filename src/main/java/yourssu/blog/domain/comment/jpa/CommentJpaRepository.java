package yourssu.blog.domain.comment.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByArticleEntity_Id(Long articleId);

    List<CommentEntity> findByUserEntity_Id(Long userId);
}
