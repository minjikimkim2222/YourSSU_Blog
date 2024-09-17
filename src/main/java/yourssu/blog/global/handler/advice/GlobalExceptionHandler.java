package yourssu.blog.global.handler.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yourssu.blog.global.exception.ResourceNotFoundException;
import yourssu.blog.global.exception.UnauthorizedAccessException;
import yourssu.blog.global.handler.ErrorResponse;

import java.nio.file.AccessDeniedException;

/*
    예외 전역 처리기
        - 예외가 발생했을 때, ErrorResponse를 JSON 형태로 반환
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    // 부적절한 때에 메서드가 불려간 것
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalStatusException(IllegalStateException ex, HttpServletRequest request){
        log.error("[exceptionHandler] IllegalStatusException :: ", ex);

        return new ErrorResponse("CONFLICT", ex.getMessage(), request.getRequestURI());
    }

    // @Valid에서 검증 실패하면, 발생하는 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("METHOD_ARGUMENT_NOT_VALID", "입력값이 잘못되었습니다.",  request.getRequestURI());
    }

    // 인자를 잘못 넘긴 경우 (일반적으로 입력값의 유효성 검사할 때)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentExceptionException(IllegalArgumentException ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("METHOD_ARGUMENT_NOT_VALID", ex.getMessage(),  request.getRequestURI());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    // 유저가 해당 리소스에 접근할 권한 없음을 처리하는 예외
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUnauthorizedAccessException(UnauthorizedAccessException ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("FORBIDDEN", ex.getMessage(), request.getRequestURI());
    }

    // 지원하지 않는 HTTP method 호출할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("METHOD_NOT_ALLOWED", "지원하지 않는 HTTP method를 호출했습니다.", request.getRequestURI());
    }

    // 마지막으로, 실수로 놓친 예외들은 여기서 공통으로 500에러로 처리됨
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleGlobalException(Exception ex, HttpServletRequest request){
        log.error("[exceptionHandler] Exception :: ", ex);

        return new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다", request.getRequestURI());
    }
}
