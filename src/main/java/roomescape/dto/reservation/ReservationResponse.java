package roomescape.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservationtime.ReservationTimeResponse;

public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time) {
    private ReservationResponse(long id, String name, LocalDate date, long timeId, LocalTime time) {
        this(id, name, date, new ReservationTimeResponse(timeId, time));
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponse(reservation.getTimeId(), reservation.getTime()));
    }
}
