package roomescape.common.exception.dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private final int status;
    private final String errorCode;
    private final String message;

    public ExceptionResponse(HttpStatus status, String errorCode, String message) {
        this.status = status.value();
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
