package roomescape.presentation;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.auth.AuthenticationException;

@RestControllerAdvice
public class RoomescapeControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(RoomescapeControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleDateTimeParseException(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage(), exception);
        Map<String, Object> parameters = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("요청 값 검증에 실패했습니다.");
        problemDetail.setProperties(Map.of("details", parameters));
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logger.error(exception.getMessage(), exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "값을 변환하는 중 오류가 발생했습니다.");
        problemDetail.setTitle("요청을 변환할 수 없습니다.");
        return problemDetail;
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleMalformedJwtException(MalformedJwtException exception) {
        logger.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ProblemDetail handleIllegalArgumentException(RuntimeException exception) {
        logger.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception) {
        logger.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 오류가 발생했습니다.");
    }
}
