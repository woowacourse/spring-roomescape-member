package roomescape.domain.theme.request;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnailUrl
) {
}
