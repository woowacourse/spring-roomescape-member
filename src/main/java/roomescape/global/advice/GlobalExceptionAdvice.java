package roomescape.global.advice;

import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice { // todo 공통 ErrorResponse로 내려주기
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(NullPointerException.class)
//    @ExceptionHandler({NullPointerException.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleNullPointerException(Exception e) {
        logger.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(500, "서버 에러입니다. 관리자에게 문의하세요");
        return ResponseEntity.internalServerError().body(errorResponse);
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

    // HttpMessageNotReadableException.class
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handle(DateTimeParseException e) {
        String message = "잘못된 날짜 혹은 시간입니다.";
        return ResponseEntity.badRequest().body(message);
    }
}
