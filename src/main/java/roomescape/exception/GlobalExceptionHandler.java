package roomescape.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<FailureResponse> handleInvalidJwtException(InvalidJwtException ex) {
        return createBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(MemberExistException.class)
    public ResponseEntity<FailureResponse> handleHttpMessageNotReadableException(MemberExistException ex) {
        return createBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<FailureResponse> handleMemberNotFoundException(MemberNotFoundException ex) {
        return createUnAuthorizedResponse(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return createBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(ReservationDuplicateException.class)
    public ResponseEntity<FailureResponse> handleReservationDuplicateException(ReservationDuplicateException ex) {
        return createBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(ReservationExistException.class)
    public ResponseEntity<FailureResponse> handleReservationExistException(ReservationExistException ex) {
        return createBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<FailureResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return createNotFoundResponse(ex.getMessage());
    }

    private ResponseEntity<FailureResponse> createUnAuthorizedResponse(String message) {
        return createFailureResponse(HttpStatus.UNAUTHORIZED, message);
    }

    private ResponseEntity<FailureResponse> createNotFoundResponse(String message) {
        return createFailureResponse(HttpStatus.NOT_FOUND, message);
    }

    private ResponseEntity<FailureResponse> createBadRequestResponse(String message) {
        return createFailureResponse(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<FailureResponse> createFailureResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(
                new FailureResponse(httpStatus, message)
        );
    }
}
