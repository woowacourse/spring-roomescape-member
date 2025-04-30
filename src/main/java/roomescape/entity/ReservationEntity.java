package roomescape.entity;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationEntity(Long id, String name, LocalDate date, ReservationTimeEntity time,
                                ThemeEntity theme) {
    public static ReservationEntity of(Long id, Reservation reservation) {
        return new ReservationEntity(id, reservation.getName(), reservation.getDate(),
                ReservationTimeEntity.of(reservation.getTime().getId(), reservation.getTime()),
                ThemeEntity.of(reservation.getTheme().getId(), reservation.getTheme()));
    }
}
