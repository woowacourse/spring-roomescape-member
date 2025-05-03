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

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<FailureResponse> handleJsonMappingException(JsonMappingException ex) {
        String errorPath = ex.getPath().stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
        return createBadRequestResponse("형식이 잘못됨 : " + errorPath);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<FailureResponse> handleHttpMessageNotReadableException(JsonParseException ex) {
        return createBadRequestResponse("Json 형식이 잘못되었습니다.");
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
        return createBadRequestResponse(ex.getMessage());
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
