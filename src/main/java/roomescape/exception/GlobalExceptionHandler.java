package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                "INVALID_INPUT_VALUE",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(MissingServletRequestParameterException e,
                                                                   HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                "MISSING_REQUEST_PARAMETER",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                          HttpServletRequest request) {
        String message = String.format("파라미터 '%s'의 타입이 일치하지 않습니다. 입력값: '%s'", e.getName(), e.getValue());
        return createErrorResponse(HttpStatus.BAD_REQUEST, message, "TYPE_MISMATCH", request.getRequestURI());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e,
                                                               HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                "요청하신 경로를 찾을 수 없습니다: " + e.getResourcePath(),
                "PATH_NOT_FOUND",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleUnsatisfiedServletRequestParameter(UnsatisfiedServletRequestParameterException e,
                                                                                  HttpServletRequest request) {
        String message = "요청 파라미터 조건이 맞지 않습니다. 필수 파라미터: " + Arrays.toString(e.getParamConditions());
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                "INVALID_PARAMETER_CONDITION",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                      HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "요청 본문(Body)을 읽을 수 없습니다. 데이터가 누락되었거나 형식이 올바르지 않습니다.",
                "MALFORMED_JSON",
                request.getRequestURI()
        );
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e,
                                                                         HttpServletRequest request) {
        String message = String.format("지원하지 않는 Content-Type입니다. 지원되는 타입: %s", e.getSupportedMediaTypes());
        return createErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                message,
                "UNSUPPORTED_MEDIA_TYPE",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnexpectedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            HttpStatus status, String message, String errorCode, String path) {
        ErrorResponse errorResponse = ErrorResponse.from(
                status.value(),
                message,
                errorCode,
                path
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
