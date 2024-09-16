package yourssu.blog.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yourssu.blog.domain.article.controller.dto.Request;
import yourssu.blog.domain.article.controller.dto.Response;
import yourssu.blog.domain.article.service.ArticleService;

import java.util.Locale;

@Tag(name = "ArticleApiController", description = "게시글 Api 서비스 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @Operation(
            summary = "게시글 작성 API",
            description = "title 필드와 content 필드는 빈 문자열이나 NULL값을 허용하지 않습니다."
    )
    @PostMapping
    public ResponseEntity<Response> createArticle(@Valid @RequestBody Request request){
        Response response = articleService.createArticle(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
