package roomescape.common.dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private final boolean ok;
    private final int status;
    private final String errorCode;
    private final String message;

    public ExceptionResponse(HttpStatus status, String errorCode, String message) {
        this.ok = false;
        this.status = status.value();
        this.errorCode = errorCode;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
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
