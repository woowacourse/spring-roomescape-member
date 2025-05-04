package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InvalidInputException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.exception.dto.ErrorMessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_PREFIX = "[ERROR] ";

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleExistedDuplicateValueException(ExistedDuplicateValueException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ERROR_PREFIX + e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(ERROR_PREFIX + e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleNotExistedValueException(NotFoundValueException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponse(ERROR_PREFIX + e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handlePharmaceuticalViolationException(
            BusinessRuleViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessageResponse(ERROR_PREFIX + e.getMessage()));
    }
}
