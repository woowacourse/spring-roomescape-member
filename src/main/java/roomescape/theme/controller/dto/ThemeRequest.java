package roomescape.theme.controller.dto;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
}
