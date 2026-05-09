package roomescape.repository.dto;

public record PopularThemeDto(
        Long id,
        String name,
        String description,
        String thumbnail,
        Long reservationCount
) {
}
