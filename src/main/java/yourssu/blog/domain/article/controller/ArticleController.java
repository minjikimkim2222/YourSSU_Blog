package yourssu.blog.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yourssu.blog.domain.article.controller.dto.ArticleCreateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleCreateResponse;
import yourssu.blog.domain.article.controller.dto.ArticleUpdateRequest;
import yourssu.blog.domain.article.controller.dto.ArticleUpdateResponse;
import yourssu.blog.domain.article.service.ArticleService;

@Tag(name = "ArticleApiController", description = "게시글 Api 서비스 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(
            summary = "게시글 작성 API",
            description = """
                    - title 필드와 content 필드는 빈 문자열이나 NULL값을 허용하지 않습니다.
                    - Request에 포함된 email과 password는 이미 존재하는 유저의 정보여야 하며,
                      해당 유저의 비밀번호와 일치해야 합니다.
                    """
    )
    @PostMapping
    public ResponseEntity<ArticleCreateResponse> createArticle(@Valid @RequestBody ArticleCreateRequest articleCreateRequest){
        ArticleCreateResponse articleCreateResponse = articleService.createArticle(articleCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(articleCreateResponse);
    }

    @Operation(
            summary = "게시글 수정 API",
            description = """
                    - title, content 필드는 빈 문자열이나 NULL값을 허용하지 않습니다
                    - postId를 입력해야 합니다.
                    - 자신의 게시글만 수정할 수 있습니다.
                    """
    )
    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
            @PathVariable Long articleId,
            @Valid @RequestBody ArticleUpdateRequest articleUpdateRequest
            ){
        ArticleUpdateResponse articleUpdateResponse = articleService.updateArticle(articleId, articleUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(articleUpdateResponse);
    }

//    @Operation(
//            summary = "게시글 삭제 API",
//            description = """
//                    """
//    )
//    @DeleteMapping("/{articleId}")

}
