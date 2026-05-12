package roomescape.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.exception.base.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpMessageNotReadableExceptionMapper httpMessageNotReadableExceptionMapper;

    public GlobalExceptionHandler(HttpMessageNotReadableExceptionMapper httpMessageNotReadableExceptionMapper) {
        this.httpMessageNotReadableExceptionMapper = httpMessageNotReadableExceptionMapper;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(HttpMessageNotReadableException e) {
        BusinessException businessException =
                httpMessageNotReadableExceptionMapper.resolve(e);

        return ResponseEntity
                .status(businessException.getStatus())
                .body(ErrorResponse.of(businessException));
    }
}
