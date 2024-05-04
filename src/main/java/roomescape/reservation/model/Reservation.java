package roomescape.reservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final Long id,
                       final String name,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public boolean isSameTime(final ReservationTime reservationTime) {
        return this.reservationTime.equals(reservationTime);
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isBeforeDateTimeThanNow(final LocalDateTime now) {
        if (date.isBefore(now.toLocalDate())) {
            return true;
        }

        if (date.isAfter(now.toLocalDate())) {
            return false;
        }
        return reservationTime.isBefore(now.toLocalTime());
    }
}
