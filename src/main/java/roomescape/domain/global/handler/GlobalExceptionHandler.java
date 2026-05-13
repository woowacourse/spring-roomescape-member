package roomescape.domain.global.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.domain.global.exception.custom.BadRequestException;
import roomescape.domain.global.exception.custom.ConflictException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.domain.global.exception.error.ErrorResponse;
import roomescape.domain.global.exception.custom.ForbiddenException;
import roomescape.domain.global.exception.custom.NotFoundException;
import roomescape.domain.global.exception.custom.UnprocessableEntityException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_PARAMETER_TYPE));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        Throwable cause = e.getCause();
        Optional<BadRequestException> badRequestException = findBadRequestException(e);
        if (badRequestException.isPresent()) {
            BadRequestException exception = badRequestException.get();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(exception.getErrorCode(), exception.getErrors()));
        }

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String field = invalidFormatException.getPath()
                .getFirst()
                .getFieldName();
            errors.add(ErrorDetail.of(field, invalidFormatException.getValue(), "형식이 올바르지 않습니다."));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST_BODY, errors));
    }

    private Optional<BadRequestException> findBadRequestException(Throwable throwable) {
        while (throwable != null) {
            if (throwable instanceof BadRequestException badRequestException) {
                return Optional.of(badRequestException);
            }
            throwable = throwable.getCause();
        }

        return Optional.empty();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(ErrorDetail.of(e.getHeaderName(), "필수 헤더가 누락되었습니다."));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        errors.add(ErrorDetail.of(e.getParameterName(), "필수 필드가 누락되었습니다."));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(ErrorCode.COMMON_INVALID_REQUEST, errors));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.of(e.getErrorCode(), e.getErrors()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableEntityException(UnprocessableEntityException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorResponse.of(e.getErrorCode()));
    }
}
