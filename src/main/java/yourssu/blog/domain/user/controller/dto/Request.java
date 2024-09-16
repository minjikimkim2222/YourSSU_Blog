package yourssu.blog.domain.user.controller.dto;

import lombok.Data;

@Data
public class Request {
    private String email;
    private String password;
    private String username;
}
