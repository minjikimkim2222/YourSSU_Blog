package yourssu.blog.domain.comment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDeleteRequest {
    private String email;
    private String password;
}
