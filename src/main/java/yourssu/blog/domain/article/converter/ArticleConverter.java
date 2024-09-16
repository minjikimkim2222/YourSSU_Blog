package yourssu.blog.domain.article.converter;

import yourssu.blog.domain.article.controller.dto.ArticleCreateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleCreateResponse;
import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.user.converter.UserConverter;
import yourssu.blog.domain.user.model.User;

public class ArticleConverter {
    public static ArticleEntity toEntity(Article article){
        return ArticleEntity.builder()
                .id(article.getId())
                .content(article.getContent())
                .title(article.getTitle())
                .userEntity(UserConverter.toEntity(article.getUser()))
                .build();
    }

    public static Article toArticle(ArticleEntity articleEntity){
        return Article.builder()
                .id(articleEntity.getId())
                .content(articleEntity.getContent())
                .title(articleEntity.getTitle())
                .user(UserConverter.toUser(articleEntity.getUserEntity()))
                .build();
    }

    public static Article toArticle(ArticleCreateRequest articleCreateRequest, User userByEmail){
        return Article.builder()
                .content(articleCreateRequest.getContent())
                .title(articleCreateRequest.getTitle())
                .user(userByEmail)
                .build();
    }

    public static ArticleCreateResponse toResponse(Article article){
        return ArticleCreateResponse.builder()
                .articleId(article.getId())
                .email(article.getUser().getEmail())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
