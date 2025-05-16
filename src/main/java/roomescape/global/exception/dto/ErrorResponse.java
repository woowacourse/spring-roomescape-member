package roomescape.global.exception.dto;

public record ErrorResponse(
        Integer status,
        String message
) {
}
