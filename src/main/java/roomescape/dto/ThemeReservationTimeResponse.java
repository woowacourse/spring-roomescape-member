package roomescape.dto;

public record ThemeReservationTimeResponse(
        Long id,
        String startAt,
        boolean isReserved
) {
}
