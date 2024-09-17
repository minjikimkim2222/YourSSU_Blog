package yourssu.blog.domain.comment.converter;

import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.comment.controller.dto.CommentCreateRequest;
import yourssu.blog.domain.comment.controller.dto.CommentCreateResponse;
import yourssu.blog.domain.comment.controller.dto.CommentUpdateResponse;
import yourssu.blog.domain.comment.jpa.CommentEntity;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.user.converter.UserConverter;
import yourssu.blog.domain.user.model.User;

public class CommentConverter {
    public static CommentEntity toEntity(Comment comment){
        return CommentEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .articleEntity(ArticleConverter.toEntity(comment.getArticle()))
                .userEntity(UserConverter.toEntity(comment.getUser()))
                .build();
    }

    public static Comment toComment(CommentEntity commentEntity){
        return Comment.builder()
                .id(commentEntity.getId())
                .content(commentEntity.getContent())
                .article(ArticleConverter.toArticle(commentEntity.getArticleEntity()))
                .user(UserConverter.toUser(commentEntity.getUserEntity()))
                .build();
    }

    public static Comment toComment(
            CommentCreateRequest commentCreateRequest,
            Article article,
            User user
            ){
        return Comment.builder()
                .content(commentCreateRequest.getContent())
                .article(article)
                .user(user)
                .build();
    }

    public static CommentCreateResponse toCreateResponse(Comment comment){
        return CommentCreateResponse.builder()
                .commentId(comment.getId())
                .email(comment.getUser().getEmail())
                .content(comment.getContent())
                .build();
    }

    public static CommentUpdateResponse toUpdateResponse(Comment comment){
        return CommentUpdateResponse
                .builder()
                .commentId(comment.getId())
                .email(comment.getUser().getEmail())
                .content(comment.getContent())
                .build();
    }
}
