package yourssu.blog.domain.article.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleDeleteRequest {
    private String email;
    private String password;
}
