package yourssu.blog.domain.user.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateResponse {
    private String email;
    private String username;
}
