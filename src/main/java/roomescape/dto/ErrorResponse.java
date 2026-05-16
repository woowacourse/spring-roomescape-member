package roomescape.dto;

public record ErrorResponse(int status, String errorMessage) {
    public static ErrorResponse from(int status, String errorMessage) {
        return new ErrorResponse(status, errorMessage);
    }
}
