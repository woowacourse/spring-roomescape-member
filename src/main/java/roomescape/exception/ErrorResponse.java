package roomescape.exception;

public record ErrorResponse(int status, String type, String message) {
}
