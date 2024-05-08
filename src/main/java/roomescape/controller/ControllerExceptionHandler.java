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
    public ResponseEntity<ProblemDetail> bedRequestHandleException(RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ProblemDetail> systemHandleException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(problemDetail);
    }
}
