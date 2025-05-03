package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final Theme theme;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    private Reservation(Long id, String name, Theme theme, LocalDate reservationDate, ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public static Reservation of(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, theme, date, time);
    }

    public static Reservation createNew(String name, Theme theme, LocalDate reservationDate,
                                        ReservationTime reservationTime) {
        return new Reservation(null, name, theme, reservationDate, reservationTime);
    }

    public static Reservation assignId(Long id, Reservation reservation) {
        return new Reservation(id, reservation.getName(), reservation.getTheme(), reservation.getReservationDate(),
                reservation.getReservationTime());
    }

    public boolean isPast() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime.getStartAt());
        return reservationDateTime.isBefore(now);
    }

    public boolean isDuplicatedWith(Reservation other) {
        return this.reservationDate.equals(other.reservationDate)
                && this.reservationTime.equals(other.reservationTime)
                && this.theme.equals(other.theme);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
