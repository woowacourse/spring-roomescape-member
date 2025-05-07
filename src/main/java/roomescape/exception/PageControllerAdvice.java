package roomescape.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PageControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(final Exception e, final Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/errorPage";
    }
}
