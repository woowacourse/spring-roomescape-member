package roomescape.common.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.of(exception.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        List<FieldErrorMessage> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorMessage(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(CommonErrorCode.VALIDATION_FAILED, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error("예상하지 못한 서버 예외가 발생했습니다.", exception);

        return ResponseEntity.internalServerError().build();
    }
}
