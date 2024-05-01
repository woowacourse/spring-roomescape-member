package roomescape.global.dto;

import java.util.List;
import org.springframework.validation.BindingResult;

public record ErrorResponse(int statusCode, List<FieldError> fieldErrors) {
    public ErrorResponse(int statusCode, BindingResult bindingResult) {
        this(statusCode, FieldError.from(bindingResult));
    }

    private record FieldError(String field, Object rejectedValue, String reason) {
        private static List<FieldError> from(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue(),
                            error.getDefaultMessage())
                    )
                    .toList();
        }
    }
}
