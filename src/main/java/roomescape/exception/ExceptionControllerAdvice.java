package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private final static Logger LOGGER = Logger.getGlobal();

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<String> roomEscapeException(RoomEscapeException e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        LOGGER.log(Level.WARNING, "message = {0}, class = {1}, method = {2}",
                new Object[]{e.getMessage(), stackTraceElement.getClassName(), stackTraceElement.getMethodName()}
        );
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
