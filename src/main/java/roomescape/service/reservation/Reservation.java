package roomescape.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Reservation {

    private final Long id;
    private final MemberName name;
    private final ReservationDateTime dateTime;
    private final Theme theme;

    private Reservation(final Long id, final MemberName name, final ReservationDateTime dateTime, final Theme theme) {
        validateTheme(theme);
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.theme = theme;
    }

    public Reservation(final Long id, final String name,
                       final LocalDate date, final ReservationTime time,
                       final Theme theme) {
        this(id, new MemberName(name), new ReservationDateTime(date, time), theme);
    }

    public Reservation(final String name, final LocalDate date,
                       final ReservationTime time, final Theme theme) {
        this(null, new MemberName(name), new ReservationDateTime(date, time), theme);
    }

    public Reservation(final long id, final Reservation reservation) {
        this(id, reservation.name, reservation.dateTime, reservation.theme);
    }

    private void validateTheme(final Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마를 입력해야 합니다.");
        }
    }

    public boolean isBefore(final LocalDateTime other) {
        return dateTime.isBefore(other);
    }

    public boolean isSameTime(final ReservationTime reservationTime) {
        return dateTime.isSameTime(reservationTime);
    }

    public Long getTimeId() {
        return dateTime.getTimeId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return dateTime.getDate();
    }

    public ReservationTime getTime() {
        return dateTime.getTime();
    }

    public Theme getTheme() {
        return this.theme;
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
