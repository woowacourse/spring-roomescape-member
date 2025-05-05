package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<String> handleReservationConflict(RoomescapeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        final String errorMessages = "";
        StringBuilder errorMessage;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage = new StringBuilder();
            errorMessage.append(fieldError.getField())
                    .append(" 에러 발생: ")
                    .append(fieldError.getDefaultMessage())
                    .append("\n")
                    .append(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }
}
