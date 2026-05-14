package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), errorCode.getStatus());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException e
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_BAD_REQUEST;

        String message = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse(errorCode.getMessage());


        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), message, HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            MethodArgumentNotValidException e
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_BAD_REQUEST;

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse(ErrorCode.COMMON_BAD_REQUEST.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), message, HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

}
