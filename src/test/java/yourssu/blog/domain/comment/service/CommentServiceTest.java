package yourssu.blog.domain.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.controller.dto.*;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.global.exception.UnauthorizedAccessException;
import yourssu.blog.global.service.UserVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserVerifier userVerifier;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 작성 성공 테스트")
    public void createComment() throws Exception {
        // given
        final Long articleId = 1L;
        final User user = new User(1L, "email1", "pw1", "name1");
        final Article foundArticle = new Article(articleId, "content1", "title1", user);
        final CommentCreateRequest request = new CommentCreateRequest(user.getEmail(), user.getPassword(), "댓글1");

        // 댓글이 저장된 후의 예상 댓글 지정
        final Comment savedComment = new Comment(1L, request.getContent(), foundArticle, user);

        doReturn(Optional.of(foundArticle)).when(articleRepository).findById(articleId);
        doReturn(user).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());
        doReturn(savedComment).when(commentRepository).save(any(Comment.class));

        // when
        CommentCreateResponse response = commentService.createComment(articleId, request);

        // then
        assertThat(request.getContent()).isEqualTo(response.getContent());

        // verify
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    public void updateComment() throws Exception {
        // given
        final Long commentId = 1L;
        final User user = new User(1L, "email1", "pw1", "name1");
        final Article article = new Article(1L, "content1", "title1", user);

        // 예전 댓글과 새로운 댓글 세팅
        final Comment foundComment = new Comment(commentId, "예전 댓글", article, user);
        final CommentUpdateRequest request =
                new CommentUpdateRequest(user.getEmail(), user.getPassword(), "새 댓글");

        doReturn(Optional.of(foundComment)).when(commentRepository).findById(commentId);
        doReturn(user).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());

        // when
        CommentUpdateResponse response = commentService.updateComment(commentId, request);

        // then
        // 댓글 내용이 제대로 변경되었는지
        assertThat(request.getContent()).isEqualTo(response.getContent());
    }

    @Test
    @DisplayName("자신의 댓글이 아닌 댓글을 수정하거나 삭제할 때, 예외가 발생하는지 테스트")
    public void isThrowExceptionWhenUpdateOrDeleteOtherComment() throws Exception {
        // given
        final Long commentId = 1L;
        final User commentOwner = new User(1L, "email1", "pw1", "name1");
        final User otherUser = new User(2L, "email2", "pw2", "name2");
        final Article article = new Article(1L, "content1", "title1", commentOwner);

        final Comment foundComment = new Comment(commentId, "댓글1", article, commentOwner);
        final CommentUpdateRequest updateRequest =
                new CommentUpdateRequest(otherUser.getEmail(), otherUser.getPassword(), "댓글2");
        final CommentDeleteRequest deleteRequest =
                new CommentDeleteRequest(otherUser.getEmail(), otherUser.getPassword());

        doReturn(Optional.of(foundComment)).when(commentRepository).findById(commentId);
        doReturn(otherUser).when(userVerifier).verifyUserAndPassword(updateRequest.getEmail(), updateRequest.getPassword());

        // when
        UnauthorizedAccessException updateException = assertThrows(UnauthorizedAccessException.class, () -> {
            commentService.updateComment(commentId, updateRequest);
        });

        UnauthorizedAccessException deleteException = assertThrows(UnauthorizedAccessException.class, () -> {
            commentService.deleteComment(commentId, deleteRequest);
        });


        // then
        assertThat(updateException.getMessage()).isEqualTo("자신의 댓글만 수정할 수 있습니다.");
        assertThat(deleteException.getMessage()).isEqualTo("자신의 댓글만 삭제할 수 있습니다.");
    }


}