package roomescape.web.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import roomescape.web.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldError = bindingResult.getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(fieldError.toArray(FieldError[]::new));
        logWarn(exception);

        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HandlerMethodValidationException exception) {
        List<ParameterValidationResult> allValidationResults = exception.getAllValidationResults();
        ParameterValidationResult[] parameterValidationResults =
                allValidationResults.toArray(ParameterValidationResult[]::new);
        logWarn(exception);

        return new ResponseEntity(new ErrorResponse(parameterValidationResults), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        logWarn(exception);
        return new ResponseEntity(new ErrorResponse(exception.getConstraintViolations()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(RuntimeException exception) {
        logWarn(exception);
        return new ResponseEntity(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(AuthenticationException exception) {
        logWarn(exception);
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(AuthorizationException exception) {
        logWarn(exception);
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException exception) {
        logWarn(exception);
        return new ResponseEntity<>(new ErrorResponse("읽을 수 없는 HTTP 메세지입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException exception) {
        logWarn(exception);
        return new ResponseEntity<>(new ErrorResponse("지원하지 않는 HTTP 메서드입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        logError(exception);
        return new ResponseEntity<>(new ErrorResponse("서버 오류입니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception exception) {
        logger.error(exception.getMessage());
    }

    private void logWarn(Exception exception) {
        logger.warn(exception.getMessage());
    }
}
