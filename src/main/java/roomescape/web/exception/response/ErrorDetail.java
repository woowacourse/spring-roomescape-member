package roomescape.web.exception.response;

import jakarta.validation.ConstraintViolation;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;

public record ErrorDetail(String field, String message) {

    public ErrorDetail(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }

    public ErrorDetail(ParameterValidationResult result) {
        this(result.getMethodParameter().getParameterName(),
                result.getResolvableErrors().stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ")));
    }

    public ErrorDetail(ConstraintViolation<?> constraintViolation) {
        this(StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
                        .reduce((first, second) -> second)
                        .get()
                        .toString(),
                constraintViolation.getMessage());
    }
}
