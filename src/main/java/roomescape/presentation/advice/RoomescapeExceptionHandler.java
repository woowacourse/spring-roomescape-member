package roomescape.presentation.advice;

import jakarta.annotation.Priority;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
@Priority(1)
public class RoomescapeExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RoomescapeExceptionHandler.class);

    @ExceptionHandler(DateTimeParseException.class)
    public ProblemDetail handleDateTimeParseException(DateTimeParseException exception) {
        logger.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "올바르지 않은 시간/날짜 형식입니다.");
    }

    @ExceptionHandler(RoomescapeException.class)
    public ProblemDetail handleIllegalArgumentException(RoomescapeException exception) {
        logger.error(exception.getBody().getDetail(), exception);
        return exception.getBody();
    }
}
