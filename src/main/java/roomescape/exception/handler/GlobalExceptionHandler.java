package roomescape.exception.handler;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.exception.code.ErrorCode;
import roomescape.exception.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of(ErrorCode.UNKNOWN_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        if (status.is4xxClientError()) {
            return ResponseEntity
                    .status(status)
                    .headers(headers)
                    .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST.getCode(), ex.getMessage()));
        }

        return createErrorResponseEntity(ErrorCode.UNKNOWN_SERVER_ERROR, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return createErrorResponseEntity(ErrorCode.VALIDATION_ERROR, details, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return createErrorResponseEntity(ErrorCode.NOT_SUPPORTED_METHOD, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return createErrorResponseEntity(ErrorCode.NOT_READABLE_MESSAGE, headers, status);
    }

    private ResponseEntity<Object> createErrorResponseEntity(ErrorCode errorCode, List<String> detail, HttpHeaders headers, HttpStatusCode status) {
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(ErrorResponse.of(errorCode, detail));
    }

    private ResponseEntity<Object> createErrorResponseEntity(ErrorCode errorCode, HttpHeaders headers, HttpStatusCode status) {
        return createErrorResponseEntity(errorCode, null, headers, status);
    }
}
