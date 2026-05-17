package roomescape.exception;

public record ErrorResponse(
    String code,
    String message
) {

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.name(), code.getMessage());
    }

    public static ErrorResponse from(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message);
    }
}
