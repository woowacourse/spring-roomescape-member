package roomescape.theme.repository.dto;

public record CreateThemeParams(
        String name,
        String description,
        String imageURl
) {
}
