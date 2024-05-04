package roomescape.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining());

        log.info("Bad Request: {}", errorMessage, e);
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(IllegalRequestException e) {
        log.info("Bad Request: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        log.error("Internal Server Error 발생: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버 관리자에게 문의하세요"));
    }
}
