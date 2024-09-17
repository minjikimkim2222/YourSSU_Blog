package yourssu.blog.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.controller.dto.ArticleCreateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleCreateResponse;
import yourssu.blog.domain.article.controller.dto.ArticleUpdateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleUpdateResponse;
import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;
import yourssu.blog.global.exception.UnauthorizedAccessException;

import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 비교를 위해 추가

    public ArticleCreateResponse createArticle(ArticleCreateRequest articleCreateRequest){

        User userByEmail = verifyUserAndPassword(articleCreateRequest.getEmail(), articleCreateRequest.getPassword());

        Article savedArticle = articleRepository.save(ArticleConverter.toArticle(articleCreateRequest, userByEmail));

        return ArticleConverter.toCreateResponse(savedArticle);
    }

    // 핵심은, 요청에서 받은 이메일과 비밀번호를 이용해 유저를 확인한 후, 그 유저가 해당 게시글 작성자인지 체크
    public ArticleUpdateResponse updateArticle(Long articleId, ArticleUpdateRequest articleUpdateRequest){
        // 게시글 존재 여부 확인
        Article foundArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", articleId));

        User userByEmail = verifyUserAndPassword(articleUpdateRequest.getEmail(), articleUpdateRequest.getPassword());

        // 자신의 게시글인지 확인
        if (!foundArticle.getUser().getId().equals(userByEmail.getId())){
            throw new UnauthorizedAccessException("자신의 게시글만 수정할 수 있습니다.");
        }

        foundArticle = foundArticle.editArticle(articleUpdateRequest.getContent(), articleUpdateRequest.getTitle());

        // 변경된 게시글 저장 -- 이때, JPA에서는 기존에 저장된 엔디티 ID를 기준으로 변경감지가 일어나, 업데이트가 일어난다
        articleRepository.save(foundArticle);

        return ArticleConverter.toUpdateResponse(foundArticle);

    }

    // 해당 email을 가진 User가 존재하는지, User가 존재한다면 비밀번호가 일치하는지 검사
    private User verifyUserAndPassword(String email, String password){
        // 유저 존재 여부 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("해당 email을 가진 User가 존재하지 않습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

}
