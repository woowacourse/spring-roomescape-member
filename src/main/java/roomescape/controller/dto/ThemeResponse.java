package roomescape.controller.dto;

public record ThemeResponse(
        String id,
        String name,
        String description,
        String imageUrl
) {
}
