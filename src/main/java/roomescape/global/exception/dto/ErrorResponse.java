package roomescape.global.exception.dto;

public record ErrorResponse(
        String code,
        String message
) {
}
