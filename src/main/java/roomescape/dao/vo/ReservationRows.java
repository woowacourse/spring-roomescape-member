package roomescape.dao.vo;

import roomescape.domain.Reservation;

import java.util.List;

public class ReservationRows {
    private final List<ReservationRow> reservationRows;

    public ReservationRows(List<ReservationRow> reservationRows) {
        this.reservationRows = reservationRows;
    }

    public List<Reservation> toReservations() {
        return reservationRows.stream()
                .map(ReservationRow::toReservation)
                .toList();
    }
}
