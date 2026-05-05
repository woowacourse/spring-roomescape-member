package roomescape.theme.dto.request;

public record ThemeSaveDto(
        String name,
        String description,
        String thumbnailUrl
) {
}
