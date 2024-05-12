package roomescape.controller.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.exception.InvalidAccessException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalidAccess() {
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleRuntimeException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleCheckException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException() {
        return "올바르지 않은 날짜 / 시간 형식입니다.";
    }
}
