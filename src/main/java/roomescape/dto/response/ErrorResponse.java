package roomescape.dto.response;

import org.springframework.http.HttpStatus;
import roomescape.exception.error.ErrorCode;

public record ErrorResponse(
        HttpStatus status,
        String message
) {

    public ErrorResponse(final ErrorCode errorCode) {
        this(errorCode.getStatus(), errorCode.getMessage());
    }

    public ErrorResponse(final ErrorCode errorCode, String message) {
        this(errorCode.getStatus(), message);
    }
}
