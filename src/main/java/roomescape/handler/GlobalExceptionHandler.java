package roomescape.handler;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleInternalDataBaseException() {
        return ResponseEntity.internalServerError()
                .body("데이터 저장 중 오류가 발생하였습니다.");
    }

}
