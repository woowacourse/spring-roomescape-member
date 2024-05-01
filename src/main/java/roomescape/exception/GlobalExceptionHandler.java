package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    // TODO: CustomException으로 안잡히는 문제 해결 필요
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
        System.out.println(e.getClass());
        return new ResponseEntity<>("에러입니다." + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        System.out.println(e.getClass());
        return new ResponseEntity<>("서버 에러입니다." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
