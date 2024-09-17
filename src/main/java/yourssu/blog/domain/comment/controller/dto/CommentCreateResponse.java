package yourssu.blog.domain.comment.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentCreateResponse {
    private Long commentId;
    private String email; // 어떤 유저가 쓴 댓글인지
    private String content;
}
