package roomescape.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final Map<Class<?>, String> ERROR_MESSAGES = Map.of(
            LocalTime.class, "시간 형식은 HH:mm 이어야 합니다.",
            LocalDate.class, "날짜 형식은 yyyy-MM-dd 이어야 합니다."
    );

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleRoomEscape(RoomEscapeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .getFirst()
                .getDefaultMessage();

        return new ErrorResponse(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ErrorResponse(resolveErrorMessage(e));
    }

    private String resolveErrorMessage(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            return ERROR_MESSAGES.getOrDefault(invalidFormatException.getTargetType(), e.getMessage());
        }
        return e.getMessage();
    }

    public record ErrorResponse(String errorMessage) {
    }
}
