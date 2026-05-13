package roomescape.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(ErrorDetail::from)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.of("입력값 이상", fieldErrors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.from(e.getMessage()));
    }
}
