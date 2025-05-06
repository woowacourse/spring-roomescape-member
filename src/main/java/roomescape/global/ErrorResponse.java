package roomescape.global;

public record ErrorResponse(String message) {

    public static ErrorResponse from(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
