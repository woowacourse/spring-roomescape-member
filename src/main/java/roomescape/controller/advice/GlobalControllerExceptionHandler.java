package roomescape.controller.advice;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.ReservationBusinessException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        final Throwable rootCause = e.getRootCause();
        if (rootCause instanceof DateTimeParseException dateTimeParseException) {
            final String parsedString = dateTimeParseException.getParsedString();
            return ResponseEntity.badRequest().body(parsedString + " 의 형식이 잘못됐습니다.");
        }
        return ResponseEntity.badRequest().body("json의 형식이 잘못됐습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(e.getMessage(),e);

        final String errorMessage = e.getFieldErrors().stream()
                .map(error -> error.getField() + "는 " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(ReservationBusinessException.class)
    public ResponseEntity<String> handleIllegalArgument(final ReservationBusinessException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("서버 오류입니다.");
    }
}
