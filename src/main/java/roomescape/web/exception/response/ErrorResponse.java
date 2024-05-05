package roomescape.web.exception.response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.springframework.validation.FieldError;

public record ErrorResponse(String message, List<ErrorDetail> details) {

    private static final String INVALID_VALUE_MESSAGE = "올바른 값을 입력해주세요.";

    public ErrorResponse(String message) {
        this(message, Collections.emptyList());
    }

    public ErrorResponse(FieldError[] errors) {
        this(INVALID_VALUE_MESSAGE, Arrays.stream(errors).map(ErrorDetail::new).toList());
    }

    public ErrorResponse(Set<ConstraintViolation<?>> constraintViolations) {
        this(INVALID_VALUE_MESSAGE, constraintViolations.stream()
                .map(constraintViolation -> {
                    String[] splitPath = constraintViolation.getPropertyPath()
                            .toString()
                            .split("\\.");
                    String message = constraintViolation.getMessage();

                    return new ErrorDetail(splitPath[splitPath.length - 1], message);
                })
                .toList());
    }
}
