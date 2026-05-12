package roomescape.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ProblemDetailsAdvice {

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<String> handleIllegalArgumentsException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NoSuchElementException.class
    })
    public ResponseEntity<String> handleNoSuchElementException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DuplicateKeyException.class
    })
    public ResponseEntity<String> handleDuplicateKeyException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> handleMethodArgumentNotValidException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    public ResponseEntity<String> handleDataIntegrityViolationException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
