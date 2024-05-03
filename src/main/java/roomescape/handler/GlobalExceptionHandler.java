package roomescape.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.BadRequestException;
import roomescape.handler.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadRequestException exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getClass().getName());
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }
}
