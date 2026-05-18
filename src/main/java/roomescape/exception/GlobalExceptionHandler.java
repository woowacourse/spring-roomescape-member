package roomescape.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorType errorType = e.getErrorType();

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgument(MethodArgumentNotValidException e) {
        ErrorType errorType = ErrorType.METHOD_ARGUMENT_NOT_VALID;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(MissingPathVariableException e) {
        ErrorType errorType = ErrorType.MISSING_PATH_VARIABLE;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e) {
        ErrorType errorType = ErrorType.MISSING_REQUEST_PARAMETER;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ErrorType errorType = ErrorType.HTTP_MESSAGE_NOT_READABLE;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorType errorType = ErrorType.METHOD_ARGUMENT_TYPE_MISMATCH;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        ErrorType errorType = ErrorType.CONSTRAINT_VIOLATION;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        ErrorType errorType = ErrorType.UNEXPECTED_EXCEPTION;

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(
                        errorType.getErrorMessage(),
                        errorType.getErrorCode()));
    }
}
