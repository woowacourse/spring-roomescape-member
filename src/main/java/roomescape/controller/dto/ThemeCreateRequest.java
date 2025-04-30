package roomescape.controller.dto;

public record ThemeCreateRequest(
        String name,
        String description,
        String page,
        String thumbnail
) {
}
