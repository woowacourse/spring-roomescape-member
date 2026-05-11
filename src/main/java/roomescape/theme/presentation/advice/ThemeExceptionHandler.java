package roomescape.theme.presentation.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.theme.domain.exception.ThemeNotFoundException;

@RestControllerAdvice
public class ThemeExceptionHandler {

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<String> handleThemeNotFoundException(ThemeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
