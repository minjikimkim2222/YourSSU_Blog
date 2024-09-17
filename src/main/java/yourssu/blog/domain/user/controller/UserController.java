package yourssu.blog.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yourssu.blog.domain.user.controller.dto.UserCreateRequest;
import yourssu.blog.domain.user.controller.dto.UserCreateResponse;
import yourssu.blog.domain.user.controller.dto.UserDeleteRequest;
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
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody UserCreateRequest request){
        UserCreateResponse response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "회원 탈퇴 API",
            description = """
                    - 회원 탈퇴 시, 해당 회원이 작성한 게시글, 댓글들이 모두 삭제됩니다.
                    """
    )
    @DeleteMapping
    public ResponseEntity deleteUser(@RequestBody UserDeleteRequest userDeleteRequest){
        userService.deleteUser(userDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
