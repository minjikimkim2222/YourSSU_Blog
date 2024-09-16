package yourssu.blog.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.controller.ArticleController;
import yourssu.blog.domain.article.controller.dto.Request;
import yourssu.blog.domain.article.controller.dto.Response;
import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Response createArticle(Request request){
        // 입력받은 request의 email을 가진, 유저가 존재하는지 확인
        User userByEmail = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("해당 email을 가진 User가 존재하지 않습니다."));

        Article savedArticle = articleRepository.save(ArticleConverter.toArticle(request, userByEmail));

        return ArticleConverter.toResponse(savedArticle);
    }

}
