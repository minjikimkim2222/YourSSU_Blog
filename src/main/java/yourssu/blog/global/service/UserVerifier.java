package yourssu.blog.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.exception.ResourceNotFoundException;

/*
    여러 서비스에서 공통으로 사용하는, User 검증 로직
        - 해당 email을 가진 User가 존재하는지
        - User가 존재한다면, 비밀번호가 일치하는지 검사

 */
@Service
@RequiredArgsConstructor
public class UserVerifier {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 비교를 위해 추가

    public User verifyUserAndPassword(String email, String password){
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
