package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final ReservationName reservationName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id,
                       ReservationName reservationName,
                       ReservationDate reservationDate,
                       ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.reservationName = reservationName;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public boolean isSameDate(LocalDate date) {
        return reservationDate.isSameDate(date);
    }

    public boolean isBeforeDate(LocalDate date) {
        return reservationDate.isBeforeDate(date);
    }

    public boolean isBeforeTime(LocalTime time) {
        return reservationTime.isBeforeTime(time);
    }

    public Long getId() {
        return id;
    }

    public ReservationName getName() {
        return reservationName;
    }

    public ReservationDate getDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
