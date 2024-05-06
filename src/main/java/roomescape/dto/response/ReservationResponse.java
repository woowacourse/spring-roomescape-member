package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {

    public ReservationResponse {
        validate(id, name, date, time, theme);
    }

    private void validate(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {
        if (id == null || name == null || time == null || theme == null) {
            throw new IllegalArgumentException("잘못된 응답입니다. id=" + id + ", name=" + name + ", date=" + date + ", time=" + time);
        }
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getName(),
                reservation.getDate(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()
                ));
    }
}
