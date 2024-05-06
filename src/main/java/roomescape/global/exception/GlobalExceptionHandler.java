package roomescape.global.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import roomescape.global.exception.model.ConflictException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(NoResourceFoundException exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("유효하지 않은 API 경로입니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handle(HttpRequestMethodNotSupportedException exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("유효하지 않은 HTTP 요청 메서드입니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ExceptionResponse exceptionResponse = new ExceptionResponse("유효하지 않은 요청 형식입니다.", fieldErrors);

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleDateTimeParseException(HttpMessageNotReadableException exception) {
        logger.error(exception.getMessage());

        String message = "유효하지 않은 요청 형식입니다.";
        if (exception.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String fieldName = mismatchedInputException.getPath().get(0).getFieldName();
            message = fieldName + " 필드에 유효하지 않은 값이 입력되었습니다.";
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(message);

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());

        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    // TODO: 존재하지 않는 자원에 접근하려 시도한 경우 처리 (timeId를 2793487329로 주는 경우 등) EmptyResultDataAccessException
    //      message:

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        logger.error(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse("서버 내부에서 에러가 발생했습니다.");

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
