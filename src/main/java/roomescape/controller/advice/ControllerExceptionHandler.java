package roomescape.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.CustomException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(final CustomException exception) {
        log.error("[Custom Exception]", exception);
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception
    ) {
        log.error("[Method Argument Not Valid Exception]", exception);
        String message = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.error("[Illegal Argument Exception]", exception);
        return new ResponseEntity<>("잘못된 파라미터 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(final DataAccessException exception) {
        log.error("[Data Access Exception]", exception);
        return new ResponseEntity<>("잘못된 DB 접근입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerException(final Exception exception) {
        log.error("[Exception]", exception);
        return new ResponseEntity<>("서버 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
