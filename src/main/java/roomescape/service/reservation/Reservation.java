package roomescape.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Reservation {

    private final Long id;
    private final String name;
    private final ReservationDateTime dateTime;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.dateTime = new ReservationDateTime(date, time);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank() || name.length() > 5) {
            throw new IllegalArgumentException("예약자명은 최소 1글자, 최대 5글자여야합니다.");
        }
    }

    public boolean isBefore(final LocalDateTime other) {
        return dateTime.isBefore(other);
    }

    public Long getTimeId() {
        return dateTime.getTimeId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return dateTime.getDate();
    }

    public ReservationTime getTime() {
        return dateTime.getTime();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Reservation that = (Reservation) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
