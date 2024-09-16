package yourssu.blog.global.handler;

import lombok.Data;

import java.time.LocalDateTime;

/*
    예외가 발생했을 때, API 응답으로 제공되는 객체 정의
 */
@Data
public class ErrorResponse {
    private LocalDateTime time; // 에러 발생 시각
    private String status; // HTTP 상태
    private String message; // 에러 메세지
    private String requestURL; // 요청 URL

    public ErrorResponse(String status, String message, String requestURL){
        this.time = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.requestURL = requestURL;
    }
}
