package roomescape.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.dto.response.ErrorResponse;
import roomescape.exception.error.CommonErrorCode;
import roomescape.exception.error.ErrorCode;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        return makeErrorResponseEntity(
                CommonErrorCode.INVALID_PARAMETER_ERROR,
                resolveMethodArgumentNotValidMessage(exception)
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return makeErrorResponseEntity(
                CommonErrorCode.INVALID_PARAMETER_ERROR,
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(final NoSuchElementException exception) {
        return makeErrorResponseEntity(
                CommonErrorCode.NOT_FOUND_ERROR,
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(final NoSuchElementException exception) {
        return makeErrorResponseEntity(
                CommonErrorCode.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException exception) {
        return makeErrorResponseEntity(
                CommonErrorCode.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    private String resolveMethodArgumentNotValidMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode, String exception) {
        ErrorResponse response = new ErrorResponse(errorCode, exception);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
}
