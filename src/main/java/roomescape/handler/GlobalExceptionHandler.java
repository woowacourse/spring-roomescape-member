package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.dto.ErrorResponse;
import roomescape.dto.FieldErrorResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnprocessableEntityException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                               HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorResponse(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_CONSTRAINT", request.getRequestURI(), "요청 값이 유효하지 않습니다.", fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                               HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_REQUEST", request.getRequestURI(),
                "요청 본문의 형식이 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_PARAMETER", request.getRequestURI(),
                "필수 요청 파라미터가 누락되었습니다."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_PARAMETER_TYPE", request.getRequestURI(), "요청 파라미터의 타입이 올바르지 않습니다."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e, HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableEntityException(UnprocessableEntityException e,
                                                                            HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}