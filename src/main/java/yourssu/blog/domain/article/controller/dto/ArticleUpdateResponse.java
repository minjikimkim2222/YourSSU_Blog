package yourssu.blog.domain.article.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleUpdateResponse {
    private Long articleId;
    private String email;
    private String title;
    private String content;
}
