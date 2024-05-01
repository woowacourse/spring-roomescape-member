package roomescape.global.advice;

import java.time.format.DateTimeParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        String message = e.getMessage();
        System.out.println(message); // todo 내부에 로그찍기
        return ResponseEntity.internalServerError().body("서버 에러입니다. 관리자에게 문의하세요");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatusCode().value(), e);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handle(DateTimeParseException e) {
        String message = "잘못된 날짜 혹은 시간입니다.";
        return ResponseEntity.badRequest().body(message);
    }
}
