package yourssu.blog.domain.article.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
    title, content 필드는 빈 문자열("", " ")이나 NULL값 허용하지 않는다.
 */
@Data
@AllArgsConstructor
public class Request {
    private String email; // 유저의 이메일 -- unique로 식별
    private String password;

    @NotBlank(message = "제목은 반드시 입력해야 합니다.")
    private String title;

    @NotBlank(message = "내용은 반드시 입력해야 합니다.")
    private String content;
}
