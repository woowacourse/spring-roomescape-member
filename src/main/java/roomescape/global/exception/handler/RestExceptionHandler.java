package roomescape.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.exception.dto.ErrorResponse;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ConflictException;
import roomescape.global.exception.notFound.NotFoundException;

import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(status.value(), e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(status.value(), e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(status.value(), e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequest(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> messages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        String message = String.join(", ", messages);
        ErrorResponse response = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(response);
    }
}
