package roomescape.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public record ReservationCreateRequest(String name, String date, Long timeId, Long themeId, LocalDate today, LocalTime now) {

    public static ReservationCreateRequest of(String name, String date, Long timeId, Long themeId, LocalDate today , LocalTime now) {
        return new ReservationCreateRequest(name, date, timeId, themeId, today, now);
    }

    public Reservation toDomain(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                new ReservationName(name),
                ReservationDate.from(date),
                reservationTime,
                theme
        );
    }
}
