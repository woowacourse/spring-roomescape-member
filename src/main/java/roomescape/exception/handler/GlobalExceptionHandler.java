package roomescape.exception.handler;

import static roomescape.exception.HttpStatusMapper.STATUS_MAP;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.Response;
import roomescape.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Response> handleCustomException(CustomException customException) {
        return getResponse(STATUS_MAP.get(customException.getClass()), customException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        return getResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return getResponse(HttpStatus.BAD_REQUEST, "이미 존재하는 데이터이거나 제약 조건을 위반했습니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRuntimeException() {
        return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

    private ResponseEntity<Response> getResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(Response.from(httpStatus.value(), message), httpStatus);
    }
}
