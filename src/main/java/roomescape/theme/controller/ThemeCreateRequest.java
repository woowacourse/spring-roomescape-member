package roomescape.theme.controller;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnailUrl
) {
}
