package roomescape.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleNotValidArgument(MethodArgumentNotValidException e) {
        List<FieldErrorDetail> fieldErrorDetails = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldErrorDetail::from)
                .toList();

        return ResponseEntity.status(ErrorCode.INVALID_INPUT.status()).
                body(ErrorResponse.of(ErrorCode.INVALID_INPUT, fieldErrorDetails));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableMessage(HttpMessageNotReadableException e) {
        return ResponseEntity.status(ErrorCode.INVALID_REQUEST_FORMAT.status())
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST_FORMAT));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessError(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.status())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(DomainNotValidValueException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(DomainNotValidValueException e) {
        String message = e.getMessage();
        ErrorCode error = ErrorCode.INVALID_VALUE;
        return ResponseEntity.status(error.status())
                .body(ErrorResponse.of(error, message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingQueryParameter(MissingServletRequestParameterException e) {
        return ResponseEntity.status(ErrorCode.MISSING_PARAMETER.status())
                .body(ErrorResponse.of(ErrorCode.MISSING_PARAMETER));
    }
}
