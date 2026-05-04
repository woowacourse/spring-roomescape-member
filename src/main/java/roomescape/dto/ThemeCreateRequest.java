package roomescape.dto;

public record ThemeCreateRequest(
        Long id,
        String name,
        String description,
        String imgUrl
) {
}
