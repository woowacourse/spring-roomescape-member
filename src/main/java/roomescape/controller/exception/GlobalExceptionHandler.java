package roomescape.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;
import roomescape.exception.SecureException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MissingPathVariableException.class,
            TypeMismatchException.class,
            BindException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidHttpRequest(Exception exception) {
        log.warn("[Invalid HTTP request]", exception);
        String exceptionName = exception.getClass().getSimpleName();
        ErrorResponse response = new ErrorResponse("HTTP 요청이 올바르지 않습니다. 발생한 예외 = " + exceptionName);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        log.warn("[Bad Request]", exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            InUseEntityException.class
    })
    public ResponseEntity<ErrorResponse> handleSecureBadRequest(SecureException secureException) {
        log.warn(
                "[Secure Bad Request]: {}",
                secureException.getSensitiveInformation(),
                secureException
        );
        ErrorResponse response = new ErrorResponse(secureException.getMessage());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception) {
        log.error("[Internal Server Error]", exception);
        ErrorResponse response = new ErrorResponse("예상하지 못한 예외가 발생했습니다.");

        return ResponseEntity.internalServerError()
                .body(response);
    }
}
