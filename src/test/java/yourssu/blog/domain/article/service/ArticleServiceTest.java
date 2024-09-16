package yourssu.blog.domain.article.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yourssu.blog.domain.article.controller.dto.ArticleCreateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleCreateResponse;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 작성 성공 테스트")
    public void createArticle() throws Exception {
        // given
        final String email = "user@exist.com";
        final User user = new User(1L, email, "pw", "username");
        final ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(email, "pw", "title", "content");
        final Article savedArticle = Article.builder()
                .id(1L)
                .content(articleCreateRequest.getContent())
                .title(articleCreateRequest.getTitle())
                .user(user)
                .build();


        // 유저가 존재하는 경우 - 미리 세팅한 user 반환
        doReturn(Optional.of(user)).when(userRepository).findByEmail(articleCreateRequest.getEmail());
        // 지정된 savedArticle 반환하도록
        doReturn(savedArticle).when(articleRepository).save(any(Article.class));

        // when
        final ArticleCreateResponse articleCreateResponse = articleService.createArticle(articleCreateRequest);


        // then
        assertThat(articleCreateResponse.getEmail()).isEqualTo(articleCreateRequest.getEmail());
        assertThat(articleCreateResponse.getTitle()).isEqualTo(articleCreateRequest.getTitle());
        assertThat(articleCreateResponse.getContent()).isEqualTo(articleCreateRequest.getContent());

        // verify
        verify(userRepository, times(1)).findByEmail(articleCreateRequest.getEmail());
        verify(articleRepository, times(1)).save(any(Article.class));
    }
    @Test
    @DisplayName("유저가 존재하지 않을 때, 예외 발생 테스트")
    public void UserNotFound() throws Exception {
        // given
        final String email = "no@user.com";
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(email, "pw", "title", "name");

        // 유저가 존재하지 않을 경우
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        // when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            articleService.createArticle(articleCreateRequest);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 email을 가진 User가 존재하지 않습니다.");

        // verify
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    @DisplayName("유저가 존재하지만 비밀번호가 다를 때, 예외 발생 테스트")
    public void UserExistButDifferentPW() throws Exception {
        // given
        final String email = "user1@exam.com";
        final User user = new User(1L, email, "pw1", "name1");

        // 기존 유저와 다른 비밀번호로 request를 보냄
        final ArticleCreateRequest request = new ArticleCreateRequest(email, "diffentPW", "title", "content");

        doReturn(Optional.of(user)).when(userRepository).findByEmail(email);
        doReturn(false).when(passwordEncoder).matches(request.getPassword(), user.getPassword());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.createArticle(request);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");

    }
}