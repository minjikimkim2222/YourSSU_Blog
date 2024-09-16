package yourssu.blog.domain.article.service.port;

import yourssu.blog.domain.article.model.Article;

public interface ArticleRepository {
    Article save(Article article);
}
