package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(
    Long id,
    String name,
    String date,
    String startAt,
    ThemeSummary theme
) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public record ThemeSummary(Long id, String name) {}

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getNameValue(),
            reservation.getDateValue().format(DATE_FORMATTER),
            reservation.getTime().getStartAt().format(TIME_FORMATTER),
            new ThemeSummary(reservation.getThemeId(), reservation.getTheme().getNameValue())
        );
    }
}
