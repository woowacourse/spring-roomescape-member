package roomescape.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<String> handleValidationException(BindException exception) {
        return new ResponseEntity<>(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<String> handleJsonParsingException() {
        return new ResponseEntity<>("유효하지 않은 필드가 존재합니다.", BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("서버 에러입니다.", INTERNAL_SERVER_ERROR);
    }
}
