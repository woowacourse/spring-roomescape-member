package roomescape.dto;

public record ThemeResponseDto(
    Long id,
    String name,
    String description,
    String thumbnail
) {
}
