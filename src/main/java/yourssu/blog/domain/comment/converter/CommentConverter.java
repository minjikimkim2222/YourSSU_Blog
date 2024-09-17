package yourssu.blog.domain.comment.converter;

import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.comment.jpa.CommentEntity;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.user.converter.UserConverter;

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
}
