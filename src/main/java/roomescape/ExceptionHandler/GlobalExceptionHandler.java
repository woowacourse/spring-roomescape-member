package roomescape.ExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.NotRemovableByConstraintException;
import roomescape.exception.NullRequestParameterException;
import roomescape.exception.WrongStateException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NullRequestParameterException.class)
    public ResponseEntity<String> handleNullRequestParameterException(NullRequestParameterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = NotRemovableByConstraintException.class)
    public ResponseEntity<String> handleNotRemovableByConstraintException(NotRemovableByConstraintException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = WrongStateException.class)
    public ResponseEntity<String> handleWrongStateException(WrongStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getRootCause() instanceof NullPointerException || e.getRootCause() instanceof NullRequestParameterException) {
            return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
        }
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
