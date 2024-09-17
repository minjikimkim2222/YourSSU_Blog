package yourssu.blog.domain.comment.controller.dto;

import lombok.Data;

@Data
public class CommentDeleteRequest {
    private String email;
    private String password;
}
