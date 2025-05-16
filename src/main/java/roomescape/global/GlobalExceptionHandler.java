package roomescape.global;

import java.time.DateTimeException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.util.LoginTokenParser;
import roomescape.exception.DatabaseForeignKeyException;
import roomescape.exception.InvalidTokenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class, DatabaseForeignKeyException.class})
    public String handleBadRequestException(RuntimeException e) {
        return ERROR_MESSAGE_PREFIX + e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getRootCause() instanceof DateTimeException) {
            return ERROR_MESSAGE_PREFIX + "잘못된 날짜 또는 시간 형식입니다.";
        }
        if (e.getRootCause() instanceof IllegalArgumentException) {
            return ERROR_MESSAGE_PREFIX + e.getMostSpecificCause().getMessage();
        }
        return ERROR_MESSAGE_PREFIX + "잘못된 값이 입력되었습니다.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ERROR_MESSAGE_PREFIX + "필수 정보가 누락되었습니다.";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e) {
        return ERROR_MESSAGE_PREFIX + e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public String handleInvalidTokenException(InvalidTokenException e, HttpServletResponse response) {
        Cookie cookie = new Cookie(LoginTokenParser.LOGIN_TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ERROR_MESSAGE_PREFIX + "로그인 정보가 만료되었습니다. 다시 로그인해주세요.";
    }
}
