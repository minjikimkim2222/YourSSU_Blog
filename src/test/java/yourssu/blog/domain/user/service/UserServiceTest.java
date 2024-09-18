package yourssu.blog.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.controller.dto.UserCreateRequest;
import yourssu.blog.domain.user.controller.dto.UserCreateResponse;
import yourssu.blog.domain.user.controller.dto.UserDeleteRequest;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.service.UserVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserVerifier userVerifier;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;

    // Mock되지 않은 실제 메서드 동작 어노테이션
    @Spy
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공 및 비밀번호 암호화 테스트")
    public void signUp() throws Exception {
        // given
        final String plainPw = "plainPw";
        final String encodedPw = encoder.encode(plainPw);
        final UserCreateRequest request = new UserCreateRequest("email@example.com", plainPw, "username");

        // 테스트 시나리오 -- UserRepository가 save할 때,
        // 어떤 User 객체가 인자로 전달되든 상관없이, 이 메서드는 항상 지정된, user 객체 반환
        doReturn(new User(null, request.getEmail(), encodedPw, request.getUsername())).when(userRepository).save(any(User.class));

        // when
        final UserCreateResponse response = userService.registerUser(request);

        // then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(encoder.matches(plainPw, encodedPw)).isTrue();

        // verify -- Mock된 객체의 해당 메서드가 몇 번 호출되었는지
          // UserRepository Mock 객체의 save 메서드가 한번 호출되었는지
        verify(userRepository, times(1)).save(any(User.class));
          // Encoder 객체의 encode메서드가 1번 호출되었는지
        verify(encoder, times(2)).encode(any(String.class));

    }

    @Test
    @DisplayName("이메일 중복 시, 예외 발생 테스트")
    public void isEmailDuplicated() throws Exception {
        // given
        final String duplicatedEmail = "exist@example.com";
        final UserCreateRequest request = new UserCreateRequest(duplicatedEmail, "plainPw", "username");

        // -- userRepository의 findByEmail 호출 시, 이미 존재하는 이메일 유저 반환하게끔 설정
        doReturn(Optional.of(new User(null, duplicatedEmail, "plainPw2", "username2")))
                .when(userRepository).findByEmail(duplicatedEmail);

        // when & then
        // -- userService의 registerUser 실행 시, IllegalStateException 발생하는지 테스트
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.registerUser(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다");

        // verify
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class)); // 예외 발생으로, save 로직에 도달하지 않음

    }

    @Test
    @DisplayName("회원 탈퇴 시, 해당 회원이 작성한 게시글과 댓글 모두 삭제되는지 테스트")
    public void deleteUserAndRelatedContent() throws Exception {
        // given
        final Long userId = 1L;
        final User user = new User(userId, "email1", "pw1", "name1");
        final UserDeleteRequest request = new UserDeleteRequest(user.getEmail(), user.getPassword());

        // user와 관련된, 게시글과 댓글 데이터 설정
        final Article article1 = new Article(1L, "content1", "title1", user);
        final Article article2 = new Article(2L, "content2", "title2", user);
        List<Article> articles = Arrays.asList(article1, article2);

        final Comment comment1 = new Comment(1L, "comment1", article1, user);
        final Comment comment2 = new Comment(2L, "comment2", article1, user);
        final Comment comment3 = new Comment(3L, "comment3", article2, user);
        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);

        // Mocking
        doReturn(user).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());
        doReturn(articles).when(articleRepository).findByUserId(userId);
        doReturn(comments).when(commentRepository).findByUserId(userId);

        // when
        userService.deleteUser(request);

        // then
        verify(articleRepository, times(2)).delete(any(Article.class));
        verify(commentRepository, times(3)).delete(any(Comment.class));
        verify(userRepository, times(1)).delete(any(User.class));
    }

}