package roomescape.dto;

public record ThemeRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {
}
