package roomescape.advice;

import roomescape.global.BusinessException;

public class ErrorResponse {

    private final String code;
    private final String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of (String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of (BusinessException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
