package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.entity.Reservation;

public class ReservationResponse {
    private long id;
    private String name;
    private LocalDate date;
    private ReservationTimeResponse reservationTimeResponse;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.date = reservation.getDate();
        this.reservationTimeResponse = new ReservationTimeResponse(reservation.getReservationTime());
    }
}
