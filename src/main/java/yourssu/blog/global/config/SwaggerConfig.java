package yourssu.blog.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition( // OpenAPI 명세서 기본 정보
        info = @Info(title = "blog API 명세서",
                description = "YOURSSU Backend 과제 - blog API 명세서",
                version = "v1"))
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {
        // "/v1/**" 경로에 매칭되는 "blog API v1"이라는 그룹으로 묶어서 문서화한다.
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("blog API v1")  // 그룹 이름 설정
                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정
                .build();
    }
}
