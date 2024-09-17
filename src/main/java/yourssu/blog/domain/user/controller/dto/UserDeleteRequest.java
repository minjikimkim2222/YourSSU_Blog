package yourssu.blog.domain.user.controller.dto;

import lombok.Data;
@Data
public class UserDeleteRequest {
    private String email;
    private String password;
}
