package yourssu.blog.global.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class UserVerifierTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserVerifier userVerifier;
    @Test
    @DisplayName("유저가 존재하지 않을 때, 예외 발생 테스트")
    public void isThrowExceptionWhenUserNotFound() throws Exception {
        // given
        final String email = "no@user.com";
        final String password = "pw";

        // 유저가 존재하지 않을 경우
        doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        // when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userVerifier.verifyUserAndPassword(email, password);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 email을 가진 User가 존재하지 않습니다.");

        // verify
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("유저가 존재하지만 비밀번호가 다를 때, 예외 발생 테스트")
    public void isThrowExceptionWhenPasswordIsIncorrect() throws Exception {
        // given
        final String email = "email1";
        final User user = new User(1L, email, "pw1", "name1");

        doReturn(Optional.of(user)).when(userRepository).findByEmail(email);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userVerifier.verifyUserAndPassword(email, "pw2"); // 기존 유저와 다른 비밀번호를 보냄
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");

        // verify
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches("pw2", "pw1");

    }
}