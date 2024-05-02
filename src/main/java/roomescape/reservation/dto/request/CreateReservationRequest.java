package roomescape.reservation.dto.request;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public record CreateReservationRequest(LocalDate date, String name, Long timeId, Long themeId) {
    public CreateReservationRequest {
        if (date == null || name.isBlank() || timeId == null) {
            throw new IllegalArgumentException("올바른 예약이 아닙니다.");
        }
    }

    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(
                null,
                this.name,
                this.date,
                reservationTime,
                theme);
    }
}
