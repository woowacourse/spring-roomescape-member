package roomescape.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("토큰이 만료 되었습니다."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(final ConflictException e) {
        return createErrorResponse(HttpStatus.CONFLICT, e);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
//        return ResponseEntity.internalServerError().build();
//    }

    public ResponseEntity<ErrorResponse> createErrorResponse(final HttpStatus httpStatus, final Exception e) {
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(e.getMessage()));
    }
}
