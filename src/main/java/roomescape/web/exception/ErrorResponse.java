package roomescape.web.exception;

import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

public record ErrorResponse(String message, List<ErrorDetail> details) {
    private static final String INVALID_VALUE_MESSAGE = "잘못된 값이 입력되었습니다";

    public ErrorResponse(String message) {
        this(message, Collections.emptyList());
    }

    public ErrorResponse(List<FieldError> errors) {
        this(INVALID_VALUE_MESSAGE, errors.stream().map(ErrorDetail::new).toList());
    }
}
