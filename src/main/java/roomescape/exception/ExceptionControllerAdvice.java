package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final static Logger LOGGER = Logger.getGlobal();

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<String> roomEscapeException(RoomEscapeException e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[1];
        LOGGER.log(Level.WARNING, "class = {0}, method = {1}, message = {2}",
                new Object[]{stackTraceElement.getClassName(), stackTraceElement.getMethodName(), e.getMessage()}
        );
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> internalServerException(Exception e) {
        LOGGER.log(Level.WARNING, "알 수 없는 오류가 발생했습니다.", e);
        return ResponseEntity.internalServerError().body("[ERROR] 알 수 없는 오류가 발생했습니다.");
    }
}
