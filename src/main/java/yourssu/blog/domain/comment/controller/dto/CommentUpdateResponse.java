package yourssu.blog.domain.comment.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentUpdateResponse {
    private Long commentId;
    private String email;
    private String content;
}
