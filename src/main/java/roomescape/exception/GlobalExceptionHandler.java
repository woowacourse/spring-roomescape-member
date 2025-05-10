package roomescape.exception;

import org.springframework.http.ResponseEntity;
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

    private FailureResponse getFailureResponse(CustomException ex) {
        return new FailureResponse(ex.getHttpStatus(), ex.getMessage());
    }
}
