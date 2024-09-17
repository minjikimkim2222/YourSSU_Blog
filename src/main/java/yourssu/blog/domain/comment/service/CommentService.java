package yourssu.blog.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.controller.dto.CommentCreateRequest;
import yourssu.blog.domain.comment.controller.dto.CommentCreateResponse;
import yourssu.blog.domain.comment.controller.dto.CommentUpdateRequest;
import yourssu.blog.domain.comment.controller.dto.CommentUpdateResponse;
import yourssu.blog.domain.comment.converter.CommentConverter;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.global.exception.ResourceNotFoundException;
import yourssu.blog.global.exception.UnauthorizedAccessException;
import yourssu.blog.global.service.UserVerifier;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserVerifier userVerifier;

    public CommentCreateResponse createComment(Long articleId, CommentCreateRequest commentCreateRequest){
        // 게시글 존재 여부 확인 (pathVariable -- articleId)
        Article foundArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", articleId));

        // 유저 검증 (CommentCreateRequest -- email, password)
        User userByEmail = userVerifier.verifyUserAndPassword(commentCreateRequest.getEmail(), commentCreateRequest.getPassword());

        // 찾은 Article, User 연관관계를 바탕으로, Comment 저장
        Comment savedComment = commentRepository.save(CommentConverter.toComment(commentCreateRequest, foundArticle, userByEmail));

        return CommentConverter.toCreateResponse(savedComment);
    }

    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest){
        // 댓글 존재 여부 확인 (pathVariable -- commentId)
        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));

        // request로부터 받은 유저 검증 후, 리턴
        User userByEmail = userVerifier.verifyUserAndPassword(commentUpdateRequest.getEmail(), commentUpdateRequest.getPassword());

        // 자신의 댓글인지 확인
        if (!foundComment.getUser().getId().equals(userByEmail.getId())){
            throw new UnauthorizedAccessException("자신의 댓글만 수정할 수 있습니다.");
        }

        foundComment = foundComment.editComment(commentUpdateRequest);

        // 변경된 게시글 저장
        commentRepository.save(foundComment);

        return CommentConverter.toUpdateResponse(foundComment);
    }

}
