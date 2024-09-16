package yourssu.blog.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yourssu.blog.domain.user.controller.dto.Request;
import yourssu.blog.domain.user.controller.dto.Response;
import yourssu.blog.domain.user.service.UserService;

@Tag(name = "UserApiController", description = "사용자 Api 서비스 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "회원가입 API",
            description = "비밀번호를 암호화한 뒤, 회원가입을 합니다."
    )
    @PostMapping
    public ResponseEntity<Response> registerUser(@RequestBody Request request){
        Response response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
