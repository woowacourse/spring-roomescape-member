package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import roomescape.domain.exception.AuthFailException;
import roomescape.domain.exception.IllegalNullArgumentException;
import roomescape.domain.exception.IllegalRequestArgumentException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({IllegalNullArgumentException.class, IllegalRequestArgumentException.class})
    public ResponseEntity<ProblemDetail> bedRequestHandleException(RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ProblemDetail> systemHandleException(Exception e) {
        System.out.println(e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(problemDetail);
    }

    @ExceptionHandler({AuthFailException.class})
    public ResponseEntity<ProblemDetail> authFailException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NON_AUTHORITATIVE_INFORMATION, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .body(problemDetail);
    }
}
