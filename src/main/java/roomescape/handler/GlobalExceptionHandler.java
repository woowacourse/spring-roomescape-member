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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ExceptionDto> handleIncorrectResultSizeDataAccessException() {
        return ResponseEntity.badRequest().body(new ExceptionDto("해당 예약 시간 혹은 테마가 존재하지 않습니다."));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ExceptionDto> handleDateTimeException() {
        return ResponseEntity.badRequest().body(new ExceptionDto(
                "날짜 혹은 시간 입력 양식이 잘못되었습니다. ex)YYYY-mm-dd, HH:mm"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDto>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<ExceptionDto> exceptionDtos = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(ExceptionDto::new)
                .toList();
        return ResponseEntity.badRequest().body(exceptionDtos);
    }
}
