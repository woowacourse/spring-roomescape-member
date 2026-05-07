package roomescape.advice;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String errorCode, String message, LocalDateTime timestamp) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse of (int status, String errorCode, String message) {
        return new ErrorResponse(status, errorCode, message, LocalDateTime.now());
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
