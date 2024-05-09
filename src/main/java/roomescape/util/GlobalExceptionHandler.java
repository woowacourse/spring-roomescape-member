package roomescape.util;

import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ExceptionDto;
import roomescape.exception.InvalidInputException;
import roomescape.exception.TargetNotExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionDto> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(TargetNotExistException.class)
    public ResponseEntity<Void> handleTargetNotExistException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body(new ExceptionDto(
                "날짜 혹은 시간 입력 양식이 잘못되었습니다. ex)yyyy-MM-dd, HH:mm"));
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

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionDto> handleException() {
//        return ResponseEntity.internalServerError()
//                .body(new ExceptionDto("서버에서 요청을 처리할 수 없습니다."));
//    }
}
