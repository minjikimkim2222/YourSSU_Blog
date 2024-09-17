package yourssu.blog.domain.article.service.port;

import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.model.Article;

import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);

    Optional<Article> findById(Long id);
}
