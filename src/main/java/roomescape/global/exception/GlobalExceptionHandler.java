package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleNotValidArgument(MethodArgumentNotValidException e) {
        List<FieldErrorDetail> fieldErrorDetails = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldErrorDetail::from)
                .toList();
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(errorCode.status()).
                body(ErrorResponse.of(errorCode, fieldErrorDetails));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableMessage(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_FORMAT;
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessError(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(DomainNotValidValueException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(DomainNotValidValueException e) {
        log.error("Unexpected Domain error occurred", e);
        String message = e.getMessage();
        ErrorCode errorCode = ErrorCode.INVALID_VALUE;
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode, message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingQueryParameter(MissingServletRequestParameterException e) {
        ErrorCode errorCode = ErrorCode.MISSING_PARAMETER;
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupport(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.INVALID_PATH_VARIABLE;
        String message = String.format("'%s' 파라미터의 값이 적절하지 않습니다.", e.getName());
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("Unexpected error occurred", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }
}
