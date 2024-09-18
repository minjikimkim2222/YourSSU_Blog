package yourssu.blog.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
    - content 필드는 빈 문자열("", " ")이나 NULL값을 허용하지 않는다.
 */
@Data
@AllArgsConstructor
public class CommentUpdateRequest {
    private String email;
    private String password;

    @NotBlank(message = "내용은 반드시 입력해야 합니다.")
    private String content;
}
