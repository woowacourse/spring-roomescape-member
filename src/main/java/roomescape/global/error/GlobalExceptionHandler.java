package roomescape.global.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.global.error.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        String requiredType = Optional.ofNullable(e.getRequiredType())
            .map(Class::getSimpleName)
            .orElse("Unknown");
        List<ErrorDetail> errors = List.of(
            ErrorDetail.of(e.getName(), e.getValue(), requiredType + " 타입이어야 합니다."));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_PARAMETER_TYPE, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        List<ErrorDetail> errors = new ArrayList<>();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String field = invalidFormatException.getPath()
                .getFirst()
                .getFieldName();
            errors.add(ErrorDetail.of(
                field,
                invalidFormatException.getValue(),
                "형식이 올바르지 않습니다."
            ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST_BODY, errors));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
        MissingRequestHeaderException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(ErrorDetail.of(e.getHeaderName(), "필수 헤더가 누락되었습니다."));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(ErrorDetail.of(e.getParameterName(), "필수 필드가 누락되었습니다."));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        List<ErrorDetail> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> ErrorDetail.of(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
            )).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
        ConstraintViolationException e) {
        List<ErrorDetail> errors = e.getConstraintViolations()
            .stream()
            .map(violation -> ErrorDetail.of(
                extractFieldName(violation),
                violation.getInvalidValue(),
                violation.getMessage()
            )).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return path;
        }
        return path.substring(lastDotIndex + 1);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.of(errorCode, e.getError()));
    }
}
