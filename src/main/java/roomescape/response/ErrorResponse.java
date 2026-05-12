package roomescape.response;

public record ErrorResponse(String code, String path, String message, String action) {
}
