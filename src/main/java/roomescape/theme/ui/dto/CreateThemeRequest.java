package roomescape.theme.ui.dto;

public record CreateThemeRequest(
        String name,
        String description,
        String thumbnail
) {
}
