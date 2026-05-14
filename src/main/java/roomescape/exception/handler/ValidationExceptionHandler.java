package roomescape.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.response.ErrorResponse;
import roomescape.exception.ErrorMessage;

@RestControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());

        String errorMessage = exception.getBindingResult()
                .getAllErrors()
                .getFirst()
                .getDefaultMessage();

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error(exception.getMessage());

        return ResponseEntity.badRequest().body(new ErrorResponse(ErrorMessage.INVALID_DATA_FORMAT.getMessage()));
    }
}
