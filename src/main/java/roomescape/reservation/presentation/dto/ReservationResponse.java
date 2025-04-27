package roomescape.reservation.presentation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTimeResponse reservationTimeResponse;

    private ReservationResponse() {
    }

    public ReservationResponse(final Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName().getName();
        this.date = reservation.getDate().getReservationDate();
        this.reservationTimeResponse = new ReservationTimeResponse(reservation.getReservationTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTimeResponse getReservationTimeResponse() {
        return reservationTimeResponse;
    }

}
