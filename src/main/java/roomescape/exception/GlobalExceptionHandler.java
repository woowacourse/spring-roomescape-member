package roomescape.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomescapeBaseException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeBaseException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBodyParseError(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife) {
            return malformed(lastPathField(ife), ife.getValue(), ife.getTargetType());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("요청 본문 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParamTypeMismatch(MethodArgumentTypeMismatchException e) {
        return malformed(e.getName(), e.getValue(), e.getRequiredType());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailure(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s은(는) 필수 입력값입니다.", error.getField()))
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    private ResponseEntity<ErrorResponse> malformed(String field, Object value, Class<?> type) {
        String message = String.format(
                "'%s' 값 '%s'은(는) %s 형식이어야 합니다.",
                field, value, SupportedRequestFormat.describe(type));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    private String lastPathField(InvalidFormatException ife) {
        var path = ife.getPath();
        if (path.isEmpty()) {
            return "?";
        }
        JsonMappingException.Reference last = path.get(path.size() - 1);
        return last.getFieldName();
    }
}