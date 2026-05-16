package roomescape.exception.dto;

import java.util.List;
import roomescape.exception.code.ErrorCode;
import roomescape.exception.code.RoomEscapeErrorCode;

public record ErrorResponse(
        String code,
        List<String> details,
        String message
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, null, message);
    }

    public static ErrorResponse of(String code, List<String> details, String message) {
        return new ErrorResponse(code, details, message);
    }

    public static ErrorResponse of(RoomEscapeErrorCode roomEscapeErrorCode) {
        return new ErrorResponse(roomEscapeErrorCode.getCode(), null, roomEscapeErrorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getCode(), null, message);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<String> details) {
        return new ErrorResponse(errorCode.getCode(), details, errorCode.getMessage());
    }

}
