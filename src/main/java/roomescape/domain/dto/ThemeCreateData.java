package roomescape.domain.dto;

public record ThemeCreateData(
        String name,
        String description,
        String thumbnailUrl
) {
}
