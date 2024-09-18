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
import yourssu.blog.domain.article.controller.dto.ArticleUpdateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleUpdateResponse;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;
import yourssu.blog.global.exception.UnauthorizedAccessException;
import yourssu.blog.global.service.UserVerifier;

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
    @Mock
    private UserVerifier userVerifier;
    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 작성 성공 테스트")
    public void createArticle() throws Exception {
        // given
        final String email = "user@exist.com";
        final User user = new User(1L, email, "pw", "username");
        final ArticleCreateRequest request = new ArticleCreateRequest(email, "pw", "title", "content");
        final Article savedArticle = Article.builder()
                .id(1L)
                .content(request.getContent())
                .title(request.getTitle())
                .user(user)
                .build();


        // 유저가 검증 후, 지정한 user 반환하도록
        doReturn(user).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());

        // 지정된 savedArticle 반환하도록
        doReturn(savedArticle).when(articleRepository).save(any(Article.class));

        // when
        final ArticleCreateResponse response = articleService.createArticle(request);


        // then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getContent()).isEqualTo(request.getContent());

        // verify
        verify(userVerifier, times(1)).verifyUserAndPassword(anyString(), anyString());
        verify(articleRepository, times(1)).save(any(Article.class));
    }


    @Test
    @DisplayName("게시글 수정 성공 테스트")
    public void updateArticle() throws Exception {
        // given
        final Long articleId = 1L;
        final String userEmail = "email1";
        final String userPw = "pw1";
        final User user = new User(1L, userEmail, userPw, "name1");

        final ArticleUpdateRequest request =
                new ArticleUpdateRequest(userEmail, userPw, "new-title", "new-content");
        final Article foundArticle = new Article(articleId, "old-content", "old-title", user);

        doReturn(Optional.of(foundArticle)).when(articleRepository).findById(articleId);
        doReturn(user).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());

        // when
        ArticleUpdateResponse response = articleService.updateArticle(articleId, request);

        // then
        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        assertThat(request.getContent()).isEqualTo(response.getContent());
        assertThat(request.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("자신의 게시글이 아닌 글을 수정할 때, 예외가 발생하는지 테스트")
    public void isThrowExceptionWhenUpdateOtherArticle() throws Exception {
        // given
        final User articleWriter = new User(1L, "email1", "pw1", "name1");
        final User noWriter = new User(2L, "email2", "pw2", "name2");

        // 이미 존재하는 게시글은, articleWriter에 의해 쓰였다.
        final Article foundArticle = new Article(1L, "content1", "title1", articleWriter);

        // 요청을 보낼 때, foundArticle을 쓴 유저가 아닌 다른 유저정보를 보냈다 (noWriter)
        final ArticleUpdateRequest request =
                new ArticleUpdateRequest(noWriter.getEmail(), noWriter.getPassword(), "new-title", "new-content");

        doReturn(Optional.of(foundArticle)).when(articleRepository).findById(1L);
        doReturn(noWriter).when(userVerifier).verifyUserAndPassword(request.getEmail(), request.getPassword());

        // when
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            articleService.updateArticle(1L, request);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("자신의 게시글만 수정할 수 있습니다.");
    }
}