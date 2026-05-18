package roomescape.global.exception;

import org.springframework.validation.FieldError;

public record FieldErrorDetail(String field, String message) {
    public static FieldErrorDetail from(FieldError fieldError) {
        return new FieldErrorDetail(
                fieldError.getField(),
                fieldError.getDefaultMessage());
    }
}
