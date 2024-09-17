package yourssu.blog.domain.article.service.port;

import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);
    Optional<Article> findById(Long id);
    void deleteById(Long id);

    List<Article> findByUserId(Long userId);

    void delete(Article article);
}
