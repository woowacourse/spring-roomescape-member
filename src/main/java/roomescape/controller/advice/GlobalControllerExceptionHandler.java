package roomescape.controller.advice;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.ReservationBusinessException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Order(1)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        final String input = e.getMessage().split("\"")[1];
        return ResponseEntity.badRequest().body(input + "의 형식이 잘못되었습니다.");
    }

    @Order(2)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(e.getMessage(),e);

        final String errorMessage = e.getFieldErrors().stream()
                .map(error -> error.getField() + "는 " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @Order(3)
    @ExceptionHandler(ReservationBusinessException.class)
    public ResponseEntity<String> handleIllegalArgument(final ReservationBusinessException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Order(4)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("서버 오류입니다.");
    }
}
