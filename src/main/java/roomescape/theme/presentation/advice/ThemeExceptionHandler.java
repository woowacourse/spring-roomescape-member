package roomescape.theme.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.theme.domain.exception.ThemeNotFoundException;

@RestControllerAdvice(basePackages = "roomescape.theme")
public class ThemeExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ThemeExceptionHandler.class);

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleThemeNotFound(
            ThemeNotFoundException e, HttpServletRequest request) {
        logHandled(HttpStatus.NOT_FOUND, e, request, log);
        return response(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
