package roomescape.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.BadRequestCode;
import roomescape.exception.code.ErrorCode;
import roomescape.exception.code.NotFoundCode;
import roomescape.exception.code.ServerErrorCode;
import roomescape.response.ErrorResponse;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode code, HttpServletRequest request, String message) {
        ErrorResponse body = new ErrorResponse(
                code.name(),
                request.getRequestURI(),
                message,
                code.getAction()
        );
        return ResponseEntity.status(code.getStatus()).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationException(ConflictException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableException(UnprocessableException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BadRequestCode code = BadRequestCode.VALIDATION_FAILED;
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = code.getMessage();
        if (fieldError != null) {
            message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
        return toResponse(code, request, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        LOGGER.warn("요청 본문 파싱 실패: {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMostSpecificCause().getMessage());
        BadRequestCode code = BadRequestCode.VALIDATION_FAILED;
        return toResponse(code, request, code.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        NotFoundCode code = NotFoundCode.RESOURCE_NOT_FOUND;
        return toResponse(code, request, code.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        BadRequestCode code = BadRequestCode.INVALID_PATH;
        String message = String.format("%s %s은(는) %s이어야 합니다.",
                code.getMessage(),
                e.getName(),
                Objects.requireNonNull(e.getRequiredType()).getSimpleName());
        return toResponse(code, request, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        BadRequestCode code = BadRequestCode.OMITTED_QUERY_PARAM;
        String message = code.getMessage().formatted(e.getParameterName());
        return toResponse(code, request, message);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        LOGGER.error("예상치 못한 예외 발생: {} {}", request.getMethod(), request.getRequestURI(), e);
        ServerErrorCode code = ServerErrorCode.INTERNAL_SERVER_ERROR;
        return toResponse(code, request, code.getMessage());
    }
}
