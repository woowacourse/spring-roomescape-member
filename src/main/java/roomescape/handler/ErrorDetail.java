package roomescape.handler;

import org.springframework.validation.FieldError;

record ErrorDetail(String field, String message) {
    public ErrorDetail(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
