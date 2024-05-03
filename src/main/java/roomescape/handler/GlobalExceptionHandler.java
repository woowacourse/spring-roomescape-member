package roomescape.handler;

import java.time.DateTimeException;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ExceptionDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            DateTimeException.class,
            IllegalArgumentException.class,
            IncorrectResultSizeDataAccessException.class})
    public ResponseEntity<ExceptionDto> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDto>> handleException(MethodArgumentNotValidException e) {
        List<ExceptionDto> exceptionDtos = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(ExceptionDto::new)
                .toList();
        return ResponseEntity.badRequest().body(exceptionDtos);
    }
}
