package roomescape.handler;

import jakarta.annotation.Priority;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Priority(2)
public class BindingExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleRequestParamException(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getBody());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "요청 데이터의 타입이 올바르지 않습니다."
        );
        problemDetail.setTitle("타입 불일치");

        List<ValidationError> errors = List.of(new ValidationError(
                e.getName(),
                e.getValue(),
                e.getRequiredType().getSimpleName() + " 타입이어야 합니다."
        ));
        problemDetail.setProperty("errors", errors);

        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "요청 본문의 형식이 올바르지 않거나 필드 타입이 일치하지 않습니다."
        );
        problemDetail.setTitle("잘못된 요청 본문");
        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidException(MethodArgumentNotValidException e) {
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
