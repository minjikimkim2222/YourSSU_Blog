package yourssu.blog.domain.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserDeleteRequest {
    private String email;
    private String password;
}
