package yourssu.blog.domain.article.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yourssu.blog.domain.article.controller.dto.Request;
import yourssu.blog.domain.article.controller.dto.Response;
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
    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 작성 성공 테스트")
    public void createArticle() throws Exception {
        // given
        final String email = "user@exist.com";
        final User user = new User(1L, email, "pw", "username");
        final Request request = new Request(email, "pw", "title", "content");
        final Article savedArticle = Article.builder()
                .id(1L)
                .content(request.getContent())
                .title(request.getTitle())
                .user(user)
                .build();


        // 유저가 존재하는 경우 - 미리 세팅한 user 반환
        doReturn(Optional.of(user)).when(userRepository).findByEmail(request.getEmail());
        // 지정된 savedArticle 반환하도록
        doReturn(savedArticle).when(articleRepository).save(any(Article.class));

        // when
        final Response response = articleService.createArticle(request);


        // then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getContent()).isEqualTo(request.getContent());

        // verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(articleRepository, times(1)).save(any(Article.class));
    }
    @Test
    @DisplayName("유저가 존재하지 않을 때, 예외 발생 테스트")
    public void UserNotFound() throws Exception {
        // given
        final String email = "no@user.com";
        Request request = new Request(email, "pw", "title", "name");

        // 유저가 존재하지 않을 경우
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        // when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            articleService.createArticle(request);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 email을 가진 User가 존재하지 않습니다.");

        // verify
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(articleRepository, never()).save(any(Article.class));
    }
}