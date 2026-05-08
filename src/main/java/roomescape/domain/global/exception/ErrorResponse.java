package roomescape.domain.global.exception;

public record ErrorResponse(String message) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage());
    }
}
