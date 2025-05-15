package roomescape.exception.handler;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.ErrorResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.InvalidLoginInfoException;
import roomescape.exception.exception.PastReservationTimeException;
import roomescape.exception.exception.UnauthenticatedException;
import roomescape.exception.exception.UnauthorizedException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("토큰이 유효하지 않습니다: " + ex.getMessage()));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthenticatedException(UnauthenticatedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("로그인이 필요합니다: " + ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("관리자만 접근할 수 있습니다: " + ex.getMessage()));
    }

    @ExceptionHandler(InvalidLoginInfoException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidLoginInfoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("시간 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(final DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleDeleteNotAllowedException(final DeletionNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReservationException(final DuplicateReservationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(PastReservationTimeException.class)
    public ResponseEntity<ErrorResponse> handlePastReservationTimeException(final PastReservationTimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex));
    }
}
