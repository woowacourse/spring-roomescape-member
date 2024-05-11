package roomescape.web.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.service.auth.UnauthorizedException;
import roomescape.web.exception.response.ErrorResponse;

@ControllerAdvice
class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldError = bindingResult.getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(fieldError.toArray(FieldError[]::new));

        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        ErrorResponse errorResponse = new ErrorResponse(constraintViolations);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handle(UnauthorizedException exception) {
        return new ResponseEntity(new ErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<ErrorResponse> handle(RuntimeException exception) {
        return new ResponseEntity(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(new ErrorResponse("읽을 수 없는 HTTP 메세지입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(new ErrorResponse("지원하지 않는 HTTP 메서드입니다."), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(NoResourceFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse("존재하지 않는 요청 경로입니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse("서버 오류입니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
