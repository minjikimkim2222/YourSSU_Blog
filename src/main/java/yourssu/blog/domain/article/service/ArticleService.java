package yourssu.blog.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.controller.dto.ArticleCreateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleCreateResponse;
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
    private final PasswordEncoder passwordEncoder; // 비밀번호 비교를 위해 추가

    public ArticleCreateResponse createArticle(ArticleCreateRequest articleCreateRequest){
        // 입력받은 request의 email을 가진, 유저가 존재하는지 확인
        User userByEmail = userRepository.findByEmail(articleCreateRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("해당 email을 가진 User가 존재하지 않습니다."));

        // 유저가 존재한다면, Request의 비밀번호와 저장된 유저의 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(articleCreateRequest.getPassword(), userByEmail.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Article savedArticle = articleRepository.save(ArticleConverter.toArticle(articleCreateRequest, userByEmail));

        return ArticleConverter.toResponse(savedArticle);
    }

}
