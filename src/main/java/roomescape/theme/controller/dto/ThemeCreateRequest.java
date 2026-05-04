package roomescape.theme.controller.dto;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnailUrl
) {
}
