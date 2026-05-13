package roomescape.dto;

public record ErrorResponse(String code, String path, String message, String action) {
}
