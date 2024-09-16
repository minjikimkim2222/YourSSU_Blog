package yourssu.blog.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yourssu.blog.domain.user.controller.dto.Request;
import yourssu.blog.domain.user.controller.dto.Response;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    // Mock되지 않은 실제 메서드 동작 어노테이션
    @Spy
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공 및 비밀번호 암호화 테스트")
    public void signUp() throws Exception {
        // given
        final String plainPw = "plainPw";
        final String encodedPw = encoder.encode(plainPw);
        final Request request = new Request("email@example.com", plainPw, "username");

        // 테스트 시나리오 -- UserRepository가 save할 때,
        // 어떤 User 객체가 인자로 전달되든 상관없이, 이 메서드는 항상 지정된, user 객체 반환
        doReturn(new User(null, request.getEmail(), encodedPw, request.getUsername())).when(userRepository).save(any(User.class));

        // when
        final Response response = userService.registerUser(request);

        // then
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(encoder.matches(plainPw, encodedPw)).isTrue();

        // verify -- Mock된 객체의 해당 메서드가 몇 번 호출되었는지
          // UserRepository Mock 객체의 save 메서드가 한번 호출되었는지
        verify(userRepository, times(1)).save(any(User.class));
          // Encoder 객체의 encode메서드가 1번 호출되었는지
        verify(encoder, times(2)).encode(any(String.class));

    }

    @Test
    @DisplayName("이메일 중복 시, 예외 발생 테스트")
    public void isEmailDuplicated() throws Exception {
        // given
        final String duplicatedEmail = "exist@example.com";
        final Request request = new Request(duplicatedEmail, "plainPw", "username");

        // -- userRepository의 findByEmail 호출 시, 이미 존재하는 이메일 유저 반환하게끔 설정
        doReturn(Optional.of(new User(null, duplicatedEmail, "plainPw2", "username2")))
                .when(userRepository).findByEmail(duplicatedEmail);

        // when & then
        // -- userService의 registerUser 실행 시, IllegalStateException 발생하는지 테스트
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.registerUser(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다");

        // verify
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class)); // 예외 발생으로, save 로직에 도달하지 않음

    }

}