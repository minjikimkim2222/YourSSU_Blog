package yourssu.blog.domain.article.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findByUserEntity_Id(Long userId);
}
