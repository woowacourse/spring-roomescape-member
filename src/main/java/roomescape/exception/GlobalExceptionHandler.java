package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InvalidInputException;
import roomescape.exception.custom.NotFoundValueException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_PREFIX = "[ERROR] ";

    @ExceptionHandler
    public ResponseEntity<String> handleExistedDuplicateValueException(ExistedDuplicateValueException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ERROR_PREFIX + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_PREFIX + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotExistedValueException(NotFoundValueException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_PREFIX + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handlePharmaceuticalViolationException(BusinessRuleViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ERROR_PREFIX + e.getMessage());
    }
}
