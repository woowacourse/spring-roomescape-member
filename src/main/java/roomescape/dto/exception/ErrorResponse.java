package roomescape.dto.exception;

public record ErrorResponse(int statusCode, String url, String method, String message) {
}
