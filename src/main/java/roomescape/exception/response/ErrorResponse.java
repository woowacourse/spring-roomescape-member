package roomescape.exception.response;

import roomescape.exception.ErrorCode;

public record ErrorResponse(
        String exceptionCode,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }
}
