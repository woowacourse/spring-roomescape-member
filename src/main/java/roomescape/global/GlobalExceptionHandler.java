package roomescape.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.InvalidAccessTokenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        if (ex.getRootCause().getClass().equals(IllegalArgumentException.class)) {
            return new ExceptionResponse(ex.getRootCause().getMessage());
        }
        return new ExceptionResponse("잘못된 요청 형식입니다.");
    }

    //TODO : 여기 권한 관련 에러가 있는 게 맞을까?
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAccessTokenException.class)
    public ExceptionResponse handleAuthorizationException(RuntimeException ex) {
        return new ExceptionResponse(ex.getMessage());
    }
}
