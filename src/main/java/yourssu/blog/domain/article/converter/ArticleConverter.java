package yourssu.blog.domain.article.converter;

import yourssu.blog.domain.article.controller.dto.Request;
import yourssu.blog.domain.article.controller.dto.Response;
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

    public static Article toArticle(Request request, User userByEmail){
        return Article.builder()
                .content(request.getContent())
                .title(request.getTitle())
                .user(userByEmail)
                .build();
    }

    public static Response toResponse(Article article){
        return Response.builder()
                .articleId(article.getId())
                .email(article.getUser().getEmail())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
