package roomescape.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        List<FieldErrorMessage> errors
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getMessage(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldErrorMessage> errors) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getMessage(),
                errors
        );
    }
}
