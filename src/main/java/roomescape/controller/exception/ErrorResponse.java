package roomescape.controller.exception;

import roomescape.exception.ErrorCode;

public record ErrorResponse(
        String message,
        ErrorCode errorCode
) {
}
