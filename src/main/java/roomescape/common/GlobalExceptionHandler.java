package roomescape.common;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_PREFIX = "[ERROR] ";

    @ExceptionHandler
    public ResponseEntity<String> catchInternalServerException(Exception ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchValidationException(MethodArgumentNotValidException ex) {
        String exceptionMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        System.out.println(exceptionMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessages);
    }

    @ExceptionHandler
    public ResponseEntity<String> catchIllegalAccessException(IllegalAccessException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchBadRequestException(IllegalArgumentException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({
            SecurityException.class,
            JwtException.class
    })
    public ResponseEntity<String> catchUnauthorizedException(Exception ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(EXCEPTION_PREFIX + ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchConflictException(IllegalStateException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchNotFoundException(NoSuchElementException ex) {
        System.out.println(EXCEPTION_PREFIX + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
