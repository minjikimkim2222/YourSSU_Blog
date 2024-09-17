package yourssu.blog.domain.comment.model;

import lombok.Builder;
import lombok.Getter;
import yourssu.blog.domain.article.model.Article;
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
}
