package roomescape.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.global.exception.model.ConflictException;

import java.util.List;

// TODO: @RestControllerAdvice 사용 고려
@ControllerAdvice
public class ExceptionController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<String> handle(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ExceptionResponse exceptionResponse = new ExceptionResponse("유효하지 않은 요청 형식입니다.", fieldErrors);

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDateTimeParseException(HttpMessageNotReadableException exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("유효하지 않은 요청 형식입니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("요청한 값에 충돌이 발생했습니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    // TODO: 존재하지 않는 API 경로로 요청을 보내는 경우 처리. 400? 404?
    //      message: 유효하지 않은 요청 경로입니다.

    // TODO: 지원하지 않는 HTTP 메서드로 요청을 보낸 경우 처리. HttpRequestMethodNotSupportedException.
    //      message:

    // TODO: 존재하지 않는 자원에 접근하려 시도한 경우 처리 (timeId를 2793487329로 주는 경우 등) EmptyResultDataAccessException
    //      message:

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("서버 내부에서 에러가 발생했습니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
