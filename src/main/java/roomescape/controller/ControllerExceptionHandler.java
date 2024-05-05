package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import roomescape.domain.exception.IllegalNullArgumentException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, IllegalNullArgumentException.class})
    public ResponseEntity<ProblemDetail> handleException(RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }
}
