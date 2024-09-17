package yourssu.blog.domain.comment.model;

import lombok.Builder;
import lombok.Getter;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.comment.controller.dto.CommentUpdateRequest;
import yourssu.blog.domain.user.model.User;

@Getter
public class Comment {
    private final Long id;
    private final String content;
    private final Article article;
    private final User user;

    @Builder
    public Comment(Long id, String content, Article article, User user){
        this.id = id;
        this.content = content;
        this.article = article;
        this.user = user;
    }

    // Comment에서 content값만 변경
    public Comment editComment(CommentUpdateRequest commentUpdateRequest){
        return Comment.builder()
                .id(this.id) // -- JPA에서 수정하려면, 똑같은 ID임을 보장 !
                .content(commentUpdateRequest.getContent())
                .article(this.article)
                .user(this.user)
                .build();
    }
}
