package roomescape.reservation.domain;

import java.time.LocalDate;

public final class ReservationDate {
    private final LocalDate reservationDate;

    public ReservationDate(LocalDate reservationDate) {
        if(reservationDate == null){
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        this.reservationDate = reservationDate;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }
}
