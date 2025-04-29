package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final ReserverName reserverName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(
            Long id,
            String reserverName,
            LocalDate reservationDate,
            ReservationTime reservationTime,
            Theme theme
    ) {
        this.id = id;
        this.reserverName = new ReserverName(reserverName);
        this.reservationDate = new ReservationDate(reservationDate);
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public static Reservation create(
            String reserverName,
            LocalDate reservationDate,
            ReservationTime reservationTime,
            Theme theme
    ) {
        return new Reservation(null, reserverName, reservationDate, reservationTime, theme);
    }

    public Long getId() {
        return id;
    }

    public String getReserverName() {
        return reserverName.getName();
    }

    public LocalDate getDate() {
        return reservationDate.getDate();
    }

    public LocalTime getStartAt() {
        return reservationTime.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
