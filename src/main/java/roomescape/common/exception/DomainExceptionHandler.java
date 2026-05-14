package roomescape.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static roomescape.common.exception.GlobalErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class DomainExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception, HttpServletRequest request) {
        log.error("Domain exception occurred: ", exception);
        ErrorPolicy errorCode = exception.getErrorPolicy();

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.of(request.getRequestURI(), errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected exception occurred", exception);
        return internalServerError(request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> internalServerError(String path) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of(path, INTERNAL_SERVER_ERROR));
    }

}
