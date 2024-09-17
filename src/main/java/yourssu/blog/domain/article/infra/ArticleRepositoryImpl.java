package yourssu.blog.domain.article.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yourssu.blog.domain.article.controller.ArticleController;
import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.jpa.ArticleJpaRepository;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {
    private final ArticleJpaRepository articleJpaRepository;

    @Override
    public Article save(Article article) {
        ArticleEntity articleEntity = articleJpaRepository.save(ArticleConverter.toEntity(article));

        return ArticleConverter.toArticle(articleEntity);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleJpaRepository.findById(id)
                .map(articleEntity -> ArticleConverter.toArticle(articleEntity));
    }
    @Override
    public void deleteById(Long id) {
        articleJpaRepository.deleteById(id);
    }
}
