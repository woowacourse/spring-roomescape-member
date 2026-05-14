package roomescape.service.dto.theme;

public record CreateThemeCommand(
        String name,
        String description,
        String imagePath
) {
}
