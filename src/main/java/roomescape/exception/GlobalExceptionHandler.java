package roomescape.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        final String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError())
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("[REQUEST ERROR] " + errorMessage));
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        if (ex.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String errorMessage = getMismatchedInputExceptionMessage(mismatchedInputException);
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("[REQUEST ERROR] " + errorMessage));
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("[REQUEST ERROR] " + ex.getMessage()));
    }

    private String getMismatchedInputExceptionMessage(MismatchedInputException mismatchedInputException) {
        String type = mismatchedInputException.getTargetType().toString();
        String fieldNames = mismatchedInputException.getPath()
                .stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining(", "));
        return fieldNames + String.format("(type: %s) 필드의 값이 잘못되었습니다.", type);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("[REQUEST ERROR] " + e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("[FOUND ERROR] " + e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("[DATA ACCESS ERROR]"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("[SERVER ERROR]"));
    }
}
