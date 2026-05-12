package roomescape.theme.presentation.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.advice.ApiExceptionHandlerSupport;
import roomescape.common.dto.ErrorResponse;
import roomescape.theme.domain.exception.ThemeNotFoundException;

@RestControllerAdvice
public class ThemeExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(ThemeExceptionHandler.class);

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleThemeException(
            ThemeNotFoundException e, HttpServletRequest request) {
        return handleRoomescapeException(e, request, log);
    }
}
