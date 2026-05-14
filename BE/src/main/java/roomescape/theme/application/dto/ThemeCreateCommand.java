package roomescape.theme.application.dto;

public record ThemeCreateCommand(
        String name,
        String description,
        String thumbnail
) {
}
