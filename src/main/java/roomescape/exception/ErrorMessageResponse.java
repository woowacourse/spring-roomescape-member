package roomescape.exception;

public record ErrorMessageResponse(
    String code,
    String message
) {

    public static ErrorMessageResponse of(ErrorCode code) {
        return new ErrorMessageResponse(code.name(), code.getMessage());
    }

    public static ErrorMessageResponse from(ErrorCode code, String message) {
        return new ErrorMessageResponse(code.name(), message);
    }
}
