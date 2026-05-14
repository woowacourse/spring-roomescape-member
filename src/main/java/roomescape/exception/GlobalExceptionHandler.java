package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.dto.response.ErrorResponse;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PastReservationTimeException.class)
    public ResponseEntity<Void> handlePastReservationTime() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InUseException.class)
    public ResponseEntity<Void> handleInUse() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<Void> handleReservationAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                      HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.from(
                HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                "BAD_REQUEST",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(MissingServletRequestParameterException e,
                                                                   HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.from(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "BAD_REQUEST",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                          HttpServletRequest request) {
        String message = String.format("파라미터 '%s'의 타입이 일치하지 않습니다. 입력값: '%s'", e.getName(), e.getValue());
        ErrorResponse errorResponse = ErrorResponse.from(
                HttpStatus.BAD_REQUEST.value(),
                message,
                "TYPE_MISMATCH",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e,
                                                               HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.from(
                HttpStatus.NOT_FOUND.value(),
                "요청하신 경로를 찾을 수 없습니다: " + e.getResourcePath(),
                "PATH_NOT_FOUND",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleUnsatisfiedServletRequestParameter(UnsatisfiedServletRequestParameterException e,
                                                                                  HttpServletRequest request) {
        String message = "요청 파라미터 조건이 맞지 않습니다. 필수 파라미터: " + Arrays.toString(e.getParamConditions());
        ErrorResponse errorResponse = ErrorResponse.from(
                HttpStatus.BAD_REQUEST.value(),
                message,
                "UNSATISFIED_PARAMETERS",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }
}
