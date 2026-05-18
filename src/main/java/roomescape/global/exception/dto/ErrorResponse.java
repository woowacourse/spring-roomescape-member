package roomescape.global.exception.dto;

import roomescape.global.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message,
        String detail) {

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), detail);
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), "");
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse(null, message, "");
    }
}
