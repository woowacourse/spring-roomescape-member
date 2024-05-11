package roomescape.global.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.jsonwebtoken.security.SignatureException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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

// TODO: 예외 ENUM으로 분리 및 처리 로직 통합
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(NoResourceFoundException e) {
        logger.warn(e.getMessage());

        return new ResponseEntity(new ExceptionResponse("유효하지 않은 API 경로입니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handle(HttpRequestMethodNotSupportedException e) {
        logger.warn(e.getMessage());

        return new ResponseEntity(new ExceptionResponse("유효하지 않은 HTTP 요청 메서드입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SignatureException.class)
    public ResponseEntity<ExceptionResponse> handle(SignatureException e) {
        logger.warn(e.getMessage());

        return new ResponseEntity(new ExceptionResponse("유효하지 않은 token 정보입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException e) {
        logger.warn(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        return new ResponseEntity(
                new ExceptionResponse("유효하지 않은 요청 형식입니다.", fieldErrors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handle(HttpMessageNotReadableException e) {
        logger.warn(e.getMessage());

        String message = "유효하지 않은 요청 형식입니다.";
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String fieldName = mismatchedInputException.getPath().get(0).getFieldName();
            message = fieldName + " 필드에 유효하지 않은 값이 입력되었습니다.";
        }

        return new ResponseEntity(new ExceptionResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handle(ApplicationException e) {
        logger.warn(e.getMessage());

        return new ResponseEntity(new ExceptionResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<ExceptionResponse> handle(EmptyResultDataAccessException e) {
        logger.warn(e.getMessage());

        return new ResponseEntity<>(new ExceptionResponse("존재하지 않는 자원의 id로 접근할 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        logger.error(e.getMessage());

        return new ResponseEntity(new ExceptionResponse("서버 내부에서 에러가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
