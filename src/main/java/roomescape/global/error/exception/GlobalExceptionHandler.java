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
import roomescape.global.error.exception.dto.ErrorResponseDTO;
import roomescape.global.error.exception.dto.FieldErrorResponseDTO;
import roomescape.global.error.exception.dto.FieldErrorResponsesDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e
    ) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO("요청값이 올바르지 않습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponsesDTO> handleValidationException(
        MethodArgumentNotValidException exception
    ) {
        List<FieldErrorResponseDTO> fieldErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldErrorResponseDTO(
                error.getField(),
                error.getDefaultMessage()
            ))
            .toList();

        return ResponseEntity.badRequest().body(new FieldErrorResponsesDTO("요청 값이 올바르지 않습니다.", fieldErrors));
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationException(ReservationException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<FieldErrorResponsesDTO> handleReservationNotFoundException(ReservationNotFoundException e) {
        return ResponseEntity.status(e.getStatus())
            .body(new FieldErrorResponsesDTO(e.getMessage(), e.getFieldErrors()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDTO("예상치 못한 오류가 발생했습니다."));
    }
}
