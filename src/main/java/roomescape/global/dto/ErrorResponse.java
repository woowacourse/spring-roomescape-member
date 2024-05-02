package roomescape.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import org.springframework.validation.BindingResult;

public record ErrorResponse(
        int statusCode,
        String message,
        @JsonInclude(Include.NON_NULL)
        List<FieldError> fieldErrors
) {
    public ErrorResponse(int statusCode, BindingResult bindingResult) {
        this(statusCode, "입력이 잘못되었습니다.", FieldError.from(bindingResult));
    }

    public ErrorResponse(int statusCode, String message) {
        this(statusCode, message, null);
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
