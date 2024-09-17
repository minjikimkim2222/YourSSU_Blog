package yourssu.blog.domain.article.controller.dto;

import lombok.Data;

@Data
public class ArticleDeleteRequest {
    private String email;
    private String password;
}
