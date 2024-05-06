package roomescape.web.exception;

import org.springframework.validation.FieldError;

public record ErrorDetail(String field, String message) {
    public ErrorDetail(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
