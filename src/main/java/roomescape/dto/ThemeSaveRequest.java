package roomescape.dto;

public record ThemeSaveRequest(
        Long id,
        String name,
        String description,
        String thumbnail
) {
}
