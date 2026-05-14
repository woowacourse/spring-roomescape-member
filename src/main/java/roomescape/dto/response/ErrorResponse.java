package roomescape.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String timestamp,
        int status,
        String errorCode,
        String message,
        String path
) {

    public static ErrorResponse from(int status, String message, String errorCode, String path) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                status,
                errorCode,
                message,
                path
        );
    }
}
