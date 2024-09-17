package yourssu.blog.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yourssu.blog.domain.comment.controller.dto.*;
import yourssu.blog.domain.comment.service.CommentService;

@Tag(name = "CommentApiController", description = "댓글 Api 서비스 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @Operation(
            summary = "댓글 작성 API",
            description = """
                    - content 필드는 빈 문자열이나 NULL값을 허용하지 않습니다.
                    - Article 연관관계는 id 정보를 통해 맺습니다.
                    - articleId를 쿼리 파라미터로 받습니다.
                    """
    )
    @PostMapping("/{articleId}")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentCreateRequest commentCreateRequest
            ){
        CommentCreateResponse commentCreateResponse = commentService.createComment(articleId, commentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);
    }

    @Operation(
            summary = "댓글 수정 API",
            description = """
                    - commentId를 쿼리 파라미터로 받습니다.
                    - content 필드는 빈 문자열이나 NULL값을 허용하지 않습니다.
                    - 자신의 댓글만 수정할 수 있습니다.
                    """
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest
            ){
        CommentUpdateResponse commentUpdateResponse = commentService.updateComment(commentId, commentUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(commentUpdateResponse);
    }

    @Operation(
            summary = "댓글 삭제 API",
            description = """
                    - commentId를 쿼리 파라미터로 받습니다.
                    - 자신의 댓글만 삭제할 수 있습니다.
                    """
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(
            @PathVariable Long commentId,
            @RequestBody CommentDeleteRequest commentDeleteRequest
            ){
        commentService.deleteComment(commentId, commentDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
