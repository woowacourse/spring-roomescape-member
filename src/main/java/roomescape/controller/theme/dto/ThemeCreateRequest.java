package roomescape.controller.theme.dto;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnailUrl
) {
}
