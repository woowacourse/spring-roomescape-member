package roomescape.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception, HttpServletRequest request) {
        log.error("Domain exception occurred: ", exception);
        ErrorPolicy errorCode = exception.getErrorPolicy();

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.of(request.getRequestURI(), errorCode));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        log.error("Method Argument exception occurred", exception);
        String path = pathFrom(request);
        List<String> messages = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        if (messages.isEmpty()) {
            messages = List.of("잘못된 요청입니다.");
        }

        return ResponseEntity
                .status(statusCode)
                .headers(headers)
                .body(ErrorResponse.of(path, GlobalErrorCode.INVALID_REQUEST, messages));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected exception occurred", exception);
        return internalServerError(request.getRequestURI());
    }


    private ResponseEntity<ErrorResponse> internalServerError(String path) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of(path, GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }

    private String pathFrom(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
