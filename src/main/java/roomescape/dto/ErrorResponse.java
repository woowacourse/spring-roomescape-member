package roomescape.dto;

public record ErrorResponse(int status, String errorMessage, Object data) {
    public static ErrorResponse from(int status, String errorMessage) {
        return new ErrorResponse(status, errorMessage, null);
    }

    public static ErrorResponse from(int status, Object data) {
        return new ErrorResponse(status, null, data);
    }
}
