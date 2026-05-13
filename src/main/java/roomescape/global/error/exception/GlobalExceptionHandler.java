package roomescape.global.error.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.error.exception.ReservationNotFoundException;
import roomescape.global.error.exception.dto.ErrorResponseDto;
import roomescape.global.error.exception.dto.FieldErrorResponseDto;
import roomescape.global.error.exception.dto.FieldErrorResponsesDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e
    ) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto("요청값이 올바르지 않습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponsesDto> handleValidationException(
        MethodArgumentNotValidException exception
    ) {
        List<FieldErrorResponseDto> fieldErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldErrorResponseDto(
                error.getField(),
                error.getDefaultMessage()
            ))
            .toList();

        return ResponseEntity.badRequest().body(new FieldErrorResponsesDto("요청 값이 올바르지 않습니다.", fieldErrors));
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationException(ReservationException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<FieldErrorResponsesDto> handleReservationNotFoundException(ReservationNotFoundException e) {
        return ResponseEntity.status(e.getStatus())
            .body(new FieldErrorResponsesDto(e.getMessage(), e.getFieldErrors()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDto("예상치 못한 오류가 발생했습니다."));
    }
}
