package roomescape.web.exception.response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;

public record ErrorResponse(String message, List<ErrorDetail> details) {

    private static final String INVALID_VALUE_MESSAGE = "올바른 값을 입력해주세요.";

    public ErrorResponse(String message) {
        this(message, Collections.emptyList());
    }

    public ErrorResponse(FieldError[] errors) {
        this(INVALID_VALUE_MESSAGE, Arrays.stream(errors).map(ErrorDetail::new).toList());
    }

    public ErrorResponse(ParameterValidationResult[] parameterValidationResults) {
        this(INVALID_VALUE_MESSAGE, Arrays.stream(parameterValidationResults).map(ErrorDetail::new).toList());
    }
}
