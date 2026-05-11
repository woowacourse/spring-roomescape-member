package roomescape.admin.controller.dto;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
}
