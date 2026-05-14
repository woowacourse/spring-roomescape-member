package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.dto.ErrorDto;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getExceptionCode().getStatus())
                .body(new ErrorDto(e.getExceptionCode().getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleCustomException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(e.getMessage()));
    }
}
