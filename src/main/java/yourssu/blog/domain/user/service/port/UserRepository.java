package yourssu.blog.domain.user.service.port;

import yourssu.blog.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);

    void delete(User user);
}
