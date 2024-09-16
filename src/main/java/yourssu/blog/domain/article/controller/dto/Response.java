package yourssu.blog.domain.article.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private Long articleId;
    private String email; // 해당 Article을 작성한 User 이메일
    private String title;
    private String content;
}
