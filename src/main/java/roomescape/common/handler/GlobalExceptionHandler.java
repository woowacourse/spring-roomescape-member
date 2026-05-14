package roomescape.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.dto.ErrorDetail;
import roomescape.common.exception.ErrorInformation;
import roomescape.common.exception.GlobalExceptionInformation;
import roomescape.common.exception.RoomEscapeException;
import roomescape.common.validation.exception.RequestValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String EXCEPTION_LOG_FORMAT = "[{}] {}";
    private static final String DATA_INTEGRITY_EXCEPTION_LOG_FORMAT = "[{}] 데이터 무결성 예외 발생";
    private static final String UNKNOWN_EXCEPTION_LOG_FORMAT = "[{}] 예상치 못한 예외 발생";

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorDetail> handleRoomEscapeException(RoomEscapeException e) {
        ErrorInformation errorInformation = e.getErrorInformation();
        log.info(EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), errorInformation.getMessage());
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetail> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        ErrorInformation errorInformation = GlobalExceptionInformation.INVALID_REQUEST_BODY;
        log.info(EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e.getMessage());
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetail> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        ErrorInformation errorInformation = GlobalExceptionInformation.INVALID_ARGUMENT;
        log.info(EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e.getMessage());
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ErrorDetail> handleRequestValidationException(
            RequestValidationException e
    ) {
        ErrorInformation errorInformation = GlobalExceptionInformation.REQUEST_VALIDATION_FAILED;
        log.info(EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e.getMessage());
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetail> handleDataIntegrityViolationException(
            DataIntegrityViolationException e
    ) {
        ErrorInformation errorInformation = GlobalExceptionInformation.DATA_INTEGRITY_VIOLATION;
        log.error(DATA_INTEGRITY_EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e);
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e
    ) {
        ErrorInformation errorInformation = GlobalExceptionInformation.METHOD_NOT_SUPPORTED;
        log.info(EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e.getMessage());
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleUnknownException(Exception e) {
        ErrorInformation errorInformation = GlobalExceptionInformation.INTERNAL_SERVER_ERROR;
        log.error(UNKNOWN_EXCEPTION_LOG_FORMAT, errorInformation.getErrorCode(), e);
        return ResponseEntity.status(errorInformation.getHttpStatus())
                .body(ErrorDetail.of(errorInformation));
    }

}
