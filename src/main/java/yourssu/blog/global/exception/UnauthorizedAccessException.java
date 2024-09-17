package yourssu.blog.global.exception;

/*
    유저가 해당 리소스에 접근할 권한 없음을 처리하는 예외
 */
public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
