package yourssu.blog.domain.article.model;

import lombok.Builder;
import lombok.Getter;
import yourssu.blog.domain.user.model.User;

@Getter
public class Article {
    private final Long id;
    private final String content;
    private final String title;
    private final User user;

    @Builder
    public Article(Long id, String content, String title, User user){
        this.id = id;
        this.content = content;
        this.title = title;
        this.user = user;
    }
}
