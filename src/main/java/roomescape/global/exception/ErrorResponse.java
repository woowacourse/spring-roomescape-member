package roomescape.global.exception;

import java.util.List;

public record ErrorResponse(
        String errorCode,
        String message,
        List<FieldErrorDetail> fieldErrors
) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.errorCode(), errorCode.message(), null);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponse(errorCode.errorCode(), errorCode.message(), fieldErrors);
    }
}
