package roomescape.global;

import java.time.DateTimeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return "[ERROR] " + e.getMessage();
    }

    // TODO: 코드 개선점 고민해보기(근본 예외 타입에 따른 구체적인 조치가 필요할지?)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getRootCause() instanceof DateTimeException) {
            return "[ERROR] 잘못된 날짜 또는 시간 형식입니다.";
        }
        if (e.getRootCause() instanceof IllegalArgumentException) {
            return "[ERROR] " + e.getMostSpecificCause().getMessage();
        }
        return "[ERROR] 잘못된 값이 입력되었습니다.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return "[ERROR] 필수 정보가 누락되었습니다.";
    }
}
