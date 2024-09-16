package yourssu.blog.global.handler.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yourssu.blog.global.handler.ErrorResponse;

/*
    예외 전역 처리기
        - 예외가 발생했을 때, ErrorResponse를 JSON 형태로 반환
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalStatusException(IllegalStateException ex, HttpServletRequest request){
        log.error("[exceptionHandler] IllegalStatusException :: ", ex);

        return new ErrorResponse("CONFLICT", ex.getMessage(), request.getRequestURI());
    }

    // 마지막으로, 실수로 놓친 예외들은 여기서 공통으로 500에러로 처리됨
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleGlobalException(Exception ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다", request.getRequestURI());
    }
}
