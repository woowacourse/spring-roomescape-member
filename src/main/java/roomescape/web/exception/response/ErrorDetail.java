package roomescape.web.exception.response;

import java.util.stream.Collectors;
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
}
