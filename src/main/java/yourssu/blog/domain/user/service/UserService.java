package yourssu.blog.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.user.controller.dto.UserCreateRequest;
import yourssu.blog.domain.user.controller.dto.UserCreateResponse;
import yourssu.blog.domain.user.converter.UserConverter;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreateResponse registerUser(UserCreateRequest request){
        // 이메일 중복 체크
        if (isEmailDuplicated(request.getEmail())){
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        User registerUser = UserConverter.toUser(request);
        registerUser = userRepository.save(registerUser);

        return UserConverter.toResponse(registerUser);
    }

    private boolean isEmailDuplicated(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
