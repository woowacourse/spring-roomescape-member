package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public static Reservation createWithoutId(String name, LocalDate date, ReservationTime time) {
        return new Reservation(null, name, date, time);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
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

    public ReservationTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Reservation reservation = (Reservation) object;
        if (id != null && reservation.id != null) {
            return Objects.equals(id, reservation.id);
        }
        return Objects.equals(name, reservation.name)
                && Objects.equals(date, reservation.date) && Objects.equals(time, reservation.time);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, date, time);
    }
}
