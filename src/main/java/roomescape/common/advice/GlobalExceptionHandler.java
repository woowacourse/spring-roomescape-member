package roomescape.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledException(Exception e) {
        log.error("Unhandled Exception 발생 : ", e);
        return ResponseEntity.internalServerError().body("일시적인 서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleValidationException(BindException e) {
        log.error("Bind Exception 발생 : {}", e.getMessage());
        return getStringResponseEntity(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValid Exception 발생 : {}", e.getMessage());
        return getStringResponseEntity(e.getBindingResult());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleReservationNotFoundException(NotFoundException e) {
        log.error("Reservation Not Found Exception 발생 : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AlreadyInUseException.class)
    public ResponseEntity<String> handleReservationTimeInUseException(AlreadyInUseException e) {
        log.error("Reservatin Already In Use Exception 발생 : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    private static ResponseEntity<String> getStringResponseEntity(BindingResult e) {
        String message = e
                .getAllErrors()
                .getFirst()
                .getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
