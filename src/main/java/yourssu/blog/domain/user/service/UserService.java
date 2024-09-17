package yourssu.blog.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.controller.dto.UserCreateRequest;
import yourssu.blog.domain.user.controller.dto.UserCreateResponse;
import yourssu.blog.domain.user.controller.dto.UserDeleteRequest;
import yourssu.blog.domain.user.converter.UserConverter;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;
import yourssu.blog.global.service.UserVerifier;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserVerifier userVerifier;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

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

    // 회원 탈퇴 시, 해당 회원이 작성한 게시글 + 댓글들이 모두 삭제되도록 !!
    public void deleteUser(UserDeleteRequest userDeleteRequest){
        User userByEmail = userVerifier.verifyUserAndPassword(userDeleteRequest.getEmail(), userDeleteRequest.getPassword());

        // 유저가 작성한 게시글과 댓글이 있다면, 모두 삭제
        userByEmail.deleteUserRelatedContent(articleRepository, commentRepository);

        // 유저 삭제
        userRepository.delete(userByEmail);
    }
    private boolean isEmailDuplicated(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
