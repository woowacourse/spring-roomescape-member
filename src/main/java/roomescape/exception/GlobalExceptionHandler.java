package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<FailureResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(getFailureResponse(ex));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<FailureResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(getFailureResponse(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<FailureResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(getFailureResponse(ex));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FailureResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(getFailureResponse(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new FailureResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    private FailureResponse getFailureResponse(RoomEscapeException ex) {
        return new FailureResponse(ex.getHttpStatus(), ex.getMessage());
    }
}
