package roomescape.global.exception;

import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

public class ExceptionResponse {

    private final String message;
    private final List<String> details;

    public ExceptionResponse(String message) {
        this(message, Collections.emptyList());
    }

    public ExceptionResponse(String message, List<FieldError> fieldErrors) {
        this.message = message;
        this.details = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + "은 " + fieldError.getDefaultMessage())
                .toList();
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
