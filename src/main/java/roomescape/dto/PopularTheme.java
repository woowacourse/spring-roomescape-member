package roomescape.dto;

public record PopularTheme(
        Long id,
        String name,
        String description,
        String imgUrl,
        Long rank,
        Long reservationCount
) {
}
