package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionPageController {

    @ExceptionHandler(UnauthenticatedUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String IllegalArgExHandler() {
        return "error/403";
    }
}
