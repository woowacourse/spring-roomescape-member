package roomescape.global.exception;

public record ErrorResponseBody(
        ErrorType errorType,
        String message
) {
}
