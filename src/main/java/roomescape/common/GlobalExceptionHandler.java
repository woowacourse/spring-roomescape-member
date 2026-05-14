package roomescape.common;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.exception.BaseException;
import roomescape.common.exception.handler.FormatHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final List<FormatHandler> formatHandlers;

    public GlobalExceptionHandler(List<FormatHandler> formatHandlers) {
        this.formatHandlers = formatHandlers;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException e) {

        Throwable cause = e.getCause();

        if (cause instanceof InvalidFormatException formatException) {
            return formatHandlers.stream()
                    .filter(h -> h.isSupport(formatException))
                    .findAny()
                    .map(h -> ResponseEntity.badRequest().body(new ErrorMessage(h.handle(formatException))))
                    .orElse(ResponseEntity.badRequest().body(new ErrorMessage("잘못된 형식입니다.")));
        }

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorMessage> handleBadRequest(BaseException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorMessage(ex.getMessage()));
    }
}
