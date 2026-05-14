package roomescape.handler;

import jakarta.annotation.Priority;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.BusinessException;

@RestControllerAdvice
@Priority(1)
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleClientException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getBody());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ProblemDetail> handleDateTimeParseException(DateTimeParseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "날짜 형식이 잘못되었습니다."
        );
        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }
}
