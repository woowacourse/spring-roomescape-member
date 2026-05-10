package roomescape.service.dto;

public record CreateThemeCommand(
        String name,
        String description,
        String imageUrl
) {
}
