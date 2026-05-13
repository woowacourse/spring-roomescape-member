package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DbExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrityViolation(DataIntegrityViolationException exception) {
        log.warn("[Integrity Violation]", exception);

        ErrorResponse response = new ErrorResponse("데이터 무결성 문제가 발생했습니다.");

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessError(DataAccessException exception) {
        log.error("[Data Access]", exception);

        ErrorResponse response = new ErrorResponse("예상하지 못한 데이터베이스 문제가 발생했습니다.");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}
