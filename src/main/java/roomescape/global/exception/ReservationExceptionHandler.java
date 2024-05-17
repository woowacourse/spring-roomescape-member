package roomescape.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {

  @ExceptionHandler
  protected ResponseEntity<ErrorResponse> handleDefaultException(final RuntimeException e) {
    return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
  }
}
