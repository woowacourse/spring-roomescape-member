package roomescape.global.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public String handleBadRequest(RuntimeException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public String handleInvalidFormatException(InvalidFormatException exception) {
        return "유효하지 않은 형식입니다.: " + exception.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        return "서버 내부 오류입니다.: " + exception.getMessage();
    }

}
