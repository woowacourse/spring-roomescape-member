package roomescape.dto;

public record ErrorResponse(
        int status,
        String code,
        String message
) {
}
