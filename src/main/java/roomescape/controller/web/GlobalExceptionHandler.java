package roomescape.controller.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.InvalidAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAccessException.class)
    public String invalidAccess() {
        return "error";
    }
}
