package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ErrorStatusMapper errorStatusMapper;

    public GlobalExceptionHandler(ErrorStatusMapper errorStatusMapper) {
        this.errorStatusMapper = errorStatusMapper;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorStatusMapper.map(errorCode))
                .body(new ErrorResponse(errorCode.message()));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException exception) {
        log.error("Infrastructure exception occurred", exception);
        return internalServerError();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception) {
        log.error("Unexpected exception occurred", exception);
        return internalServerError();
    }

    private ResponseEntity<ErrorResponse> internalServerError() {
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("서버 내부에서 문제가 발생했습니다."));
    }
}
