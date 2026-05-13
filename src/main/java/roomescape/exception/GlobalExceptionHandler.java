package roomescape.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(new GlobalErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<GlobalErrorResponse> handleDuplicateReservation(DuplicateKeyException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new GlobalErrorResponse("이미 예약된 날짜/시간/테마입니다."));
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleIdNotFoundException(IdNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new GlobalErrorResponse(e.getMessage()));
    }
}
