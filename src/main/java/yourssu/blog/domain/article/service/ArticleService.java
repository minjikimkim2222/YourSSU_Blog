package yourssu.blog.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.controller.dto.*;
import yourssu.blog.domain.article.converter.ArticleConverter;
import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;
import yourssu.blog.global.exception.UnauthorizedAccessException;
import yourssu.blog.global.service.UserVerifier;

import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserVerifier userVerifier;

    public ArticleCreateResponse createArticle(ArticleCreateRequest articleCreateRequest){

        User userByEmail = userVerifier.verifyUserAndPassword(articleCreateRequest.getEmail(), articleCreateRequest.getPassword());

        Article savedArticle = articleRepository.save(ArticleConverter.toArticle(articleCreateRequest, userByEmail));

        return ArticleConverter.toCreateResponse(savedArticle);
    }

    // 핵심은, 요청에서 받은 이메일과 비밀번호를 이용해 유저를 확인한 후, 그 유저가 해당 게시글 작성자인지 체크
    public ArticleUpdateResponse updateArticle(Long articleId, ArticleUpdateRequest articleUpdateRequest){
        // 게시글 존재 여부 확인
        Article foundArticle = getArticle(articleId);

        User userByEmail = userVerifier.verifyUserAndPassword(articleUpdateRequest.getEmail(), articleUpdateRequest.getPassword());

        // 자신의 게시글인지 확인
        if (!foundArticle.getUser().getId().equals(userByEmail.getId())){
            throw new UnauthorizedAccessException("자신의 게시글만 수정할 수 있습니다.");
        }

        foundArticle = foundArticle.editArticle(articleUpdateRequest.getContent(), articleUpdateRequest.getTitle());

        // 변경된 게시글 저장 -- 이때, JPA에서는 기존에 저장된 엔디티 ID를 기준으로 변경감지가 일어나, 업데이트가 일어난다
        articleRepository.save(foundArticle);

        return ArticleConverter.toUpdateResponse(foundArticle);

    }

    public void deleteArticle(Long articleId, ArticleDeleteRequest articleDeleteRequest){
        Article foundArticle = getArticle(articleId);
        User userByEmail = userVerifier.verifyUserAndPassword(articleDeleteRequest.getEmail(), articleDeleteRequest.getPassword());

        if (!foundArticle.getUser().getId().equals(userByEmail.getId())){
            throw new UnauthorizedAccessException("자신의 게시글만 삭제할 수 있습니다.");
        }

        // 게시글뿐만 아니라, 게시글의 댓글들까지 삭제해야 합니다.
        foundArticle.deleteCommentsIfExist(commentRepository);

        // 게시글 삭제
        articleRepository.deleteById(articleId);
    }

    private Article getArticle(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", articleId));
    }
}
