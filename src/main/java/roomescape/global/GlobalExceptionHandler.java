package roomescape.global;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleValidationException(MethodArgumentNotValidException e) {
        String exceptionMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        return new ExceptionResponse(BAD_REQUEST.value(), exceptionMessage, LocalDateTime.now());
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable rootCause = e.getRootCause();
        String exceptionMessage = "잘못된 형식의 값이 입력되었습니다.";
        if (rootCause instanceof DateTimeException) {
            exceptionMessage = "잘못된 날짜 또는 시간 형식입니다.";
        }
        return new ExceptionResponse(BAD_REQUEST.value(), exceptionMessage, LocalDateTime.now());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String exceptionMessage = "필수 정보(파라미터)가 누락되었습니다";
        return new ExceptionResponse(BAD_REQUEST.value(), exceptionMessage, LocalDateTime.now());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ExceptionResponse(BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
    }
}
