package roomescape.exception;

public record ErrorResponse(String code, String path, String message, String action) {
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(
                errorCode.getCode(),
                path,
                errorCode.getMessage(),
                errorCode.getAction()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String path, String message) {
        return new ErrorResponse(
                errorCode.getCode(),
                path,
                message,
                errorCode.getAction()
        );
    }
}
