package roomescape.handler;

import jakarta.annotation.Priority;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Priority(2)
public class BindingExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleRequestParamException(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getBody());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleBindingException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        ProblemDetail body = e.getBody();
        body.setProperty("errors", errors);

        return ResponseEntity
                .status(e.getStatusCode())
                .body(body);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(HandlerMethodValidationException e) {
        List<ValidationError> errors = extractValidationErrors(e);
        e.getBody().setProperty("errors", errors);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getBody());
    }

    private List<ValidationError> extractValidationErrors(HandlerMethodValidationException e) {
        return e.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> new ValidationError(
                                result.getMethodParameter().getParameterName(),
                                result.getArgument(),
                                error.getDefaultMessage()
                        ))
                )
                .toList();
    }

    private record ValidationError(String field, Object value, String message) {
    }
}
