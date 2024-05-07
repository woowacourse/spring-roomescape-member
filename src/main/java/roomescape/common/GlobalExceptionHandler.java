package roomescape.common;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_PREFIX = "[ERROR] ";

    @ExceptionHandler
    public ResponseEntity<String> catchValidationException(MethodArgumentNotValidException ex) {
        String exceptionMessages = EXCEPTION_PREFIX + ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n" + EXCEPTION_PREFIX));

        System.out.println(exceptionMessages);
        return ResponseEntity.badRequest().body(exceptionMessages);
    }

    @ExceptionHandler
    public ResponseEntity<String> catchBadRequestException(IllegalArgumentException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EXCEPTION_PREFIX + ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchConflictException(IllegalStateException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(EXCEPTION_PREFIX + ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchNotFoundException(NoSuchElementException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EXCEPTION_PREFIX + ex.getMessage());
    }
}
