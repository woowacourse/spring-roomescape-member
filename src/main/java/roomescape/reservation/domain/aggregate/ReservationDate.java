package roomescape.reservation.domain.aggregate;

import java.time.LocalDate;

public final class ReservationDate {
    private final LocalDate reservationDate;

    public ReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }
}
