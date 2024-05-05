package roomescape.core.dto.exception;

import java.util.List;
import org.springframework.validation.BindingResult;

public class ExceptionDetails {
    private final String message;
    private final String field;
    private final String value;

    private ExceptionDetails(final String message, final String field, final String value) {
        this.message = message;
        this.field = field;
        this.value = value;
    }

    public static List<ExceptionDetails> create(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ExceptionDetails(
                        fieldError.getDefaultMessage(),
                        fieldError.getField(),
                        String.valueOf(fieldError.getRejectedValue()))
                ).toList();
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
