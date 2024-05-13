package roomescape.global.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleServerException(Exception e) {
        logger.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(500, "서버 에러입니다. 관리자에게 문의하세요");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getStatusCode().value(), e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(HttpMessageNotReadableException e) {
        logger.warn(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400, "잘못된 데이터 형식입니다");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
