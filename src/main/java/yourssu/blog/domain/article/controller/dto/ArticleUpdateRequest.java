package yourssu.blog.domain.article.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
    title, content 필드는 빈 문자열("", " ")이나 NULL값 허용하지 않는다.
 */
@Data
@AllArgsConstructor
public class ArticleUpdateRequest {
    private String email;
    private String password;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
