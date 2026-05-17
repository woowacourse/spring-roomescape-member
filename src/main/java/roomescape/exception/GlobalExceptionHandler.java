package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_REQUEST);

        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_REQUEST);

        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_REQUEST);
        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }
}
