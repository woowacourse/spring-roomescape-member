package roomescape.exception.handler;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            NotFoundResourceException.class,
            DataReferencedException.class,
            ReservationCommandException.class,
            ReservationTimeConditionException.class,
            DuplicatedReservationRequestException.class,
            ReservationException.class,
    })
    public ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), HttpErrorMapping.getHttpStatus(customException.getErrorMessage()));
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        return ResponseEntity.badRequest().body(Map.of("message", errorMessage));
    }
}
