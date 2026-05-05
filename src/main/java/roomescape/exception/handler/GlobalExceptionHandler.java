package roomescape.exception.handler;

import org.springframework.http.ResponseEntity;
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
            DuplicatedReservationRequestException.class
    })
    public ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), HttpErrorMapping.getHttpStatus(customException.getErrorMessage()));
    }
}
