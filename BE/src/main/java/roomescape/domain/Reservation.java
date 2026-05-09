package roomescape.domain;

import java.time.LocalDate;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation createRow(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public Reservation appendId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        if (this.id == null || that.id == null) {
            return false;
        }
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return System.identityHashCode(this);
        }
    }
}
