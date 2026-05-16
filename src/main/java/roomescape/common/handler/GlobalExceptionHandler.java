package roomescape.common.handler;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.dto.ErrorDetailDto;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.InternalServerException;
import roomescape.common.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetailDto> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("Invalid request body: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDetailDto.of(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청 형식입니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailDto> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("유효하지 않은 입력값입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDetailDto.of(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetailDto> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDetailDto.of(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDetailDto> handleConflict(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDetailDto.of(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailDto> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDetailDto.of(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetailDto> handleDataIntegrity(DataIntegrityViolationException e) {
        log.warn("Data integrity violation: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDetailDto.of(HttpStatus.CONFLICT.value(), "중복된 데이터가 존재합니다."));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorDetailDto> handleInternalServer(InternalServerException e) {
        log.error("Internal Server Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDetailDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailDto> handleUnknownException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDetailDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 오류가 발생했습니다."));
    }
}
