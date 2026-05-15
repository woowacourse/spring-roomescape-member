package roomescape.common;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorMessage(message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorMessage("잘못된 형식의 값입니다: " + e.getName()));
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
