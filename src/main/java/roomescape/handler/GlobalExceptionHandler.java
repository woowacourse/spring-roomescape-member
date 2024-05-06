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

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getFieldErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, "잘못된 형식의 Request Body 입니다.");
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버에서 예기치 못한 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }
}
