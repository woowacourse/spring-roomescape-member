package roomescape.controller.dto;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String imageUrl
) {
}
