package roomescape.service.dto.request;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnailUrl
) {
}
