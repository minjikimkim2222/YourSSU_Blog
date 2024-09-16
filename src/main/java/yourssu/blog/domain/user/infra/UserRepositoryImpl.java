package yourssu.blog.domain.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yourssu.blog.domain.user.converter.UserConverter;
import yourssu.blog.domain.user.jpa.UserEntity;
import yourssu.blog.domain.user.jpa.UserJpaRepository;
import yourssu.blog.domain.user.model.User;
import yourssu.blog.domain.user.service.port.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(UserConverter.toEntity(user));
        return UserConverter.toUser(userEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserConverter::toUser);
    }
}
