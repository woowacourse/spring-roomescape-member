package roomescape.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(value = ReservationException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationException(ReservationException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = ReservationThemeException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationThemeException(ReservationThemeException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = ReservationTimeException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationTimeException(ReservationTimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(getFieldErrorMessages(exception)));
    }

    private String getFieldErrorMessages(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((first, second) -> first + ", " + second)
                .orElse("유효성 검사 오류");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto("서버 오류가 발생했습니다. %s".formatted(exception.getMessage())));
    }
}
