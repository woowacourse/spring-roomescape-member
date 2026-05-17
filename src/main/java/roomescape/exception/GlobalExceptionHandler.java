package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(
            RoomescapeException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        return createResponse(errorCode, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(errorCode.getMessage());

        return createResponse(errorCode, request, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

        return createResponse(errorCode, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String message = "필수 요청 값이 누락되었습니다: " + exception.getParameterName();

        return createResponse(errorCode, request, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String message = "요청 값의 형식이 올바르지 않습니다: " + exception.getName();

        return createResponse(errorCode, request, message);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        return createResponse(errorCode, request, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        return createResponse(errorCode, request, exception.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.API_NOT_FOUND;

        return createResponse(errorCode, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        return createResponse(errorCode, request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.UNSUPPORTED_MEDIA_TYPE;

        return createResponse(errorCode, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return createResponse(errorCode, request);
    }

    private ResponseEntity<ErrorResponse> createResponse(ErrorCode errorCode, HttpServletRequest request) {
        return ResponseEntity
                .status(toHttpStatus(errorCode))
                .body(ErrorResponse.from(errorCode, request.getRequestURI()));
    }

    private ResponseEntity<ErrorResponse> createResponse(
            ErrorCode errorCode,
            HttpServletRequest request,
            String message
    ) {
        return ResponseEntity
                .status(toHttpStatus(errorCode))
                .body(ErrorResponse.of(errorCode, request.getRequestURI(), message));
    }

    private HttpStatus toHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case INVALID_INPUT, INVALID_REQUEST_BODY, RESERVATION_PAST_TIME, RESERVATION_ALREADY_PAST ->
                    HttpStatus.BAD_REQUEST;
            case UNSUPPORTED_MEDIA_TYPE -> HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            case METHOD_NOT_ALLOWED -> HttpStatus.METHOD_NOT_ALLOWED;
            case API_NOT_FOUND, RESERVATION_NOT_FOUND, RESERVATION_TIME_NOT_FOUND, THEME_NOT_FOUND ->
                    HttpStatus.NOT_FOUND;
            case RESERVATION_DUPLICATED, RESERVATION_TIME_IN_USE, THEME_IN_USE -> HttpStatus.CONFLICT;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
