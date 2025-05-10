package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    private Reservation(final Long id, final LocalDate date, final ReservationTime time,
                        final Theme theme, final Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public static Reservation load(final Long id, final LocalDate date, final ReservationTime time, final Theme theme,
                                   final Member member) {
        return new Reservation(id, date, time, theme, member);
    }

    public static Reservation create(final LocalDate date, final ReservationTime time, final Theme theme,
                                     final Member member) {
        validateDateTime(date, time);
        return new Reservation(null, date, time, theme, member);
    }

    private static void validateDateTime(final LocalDate localDate, final ReservationTime reservationTime) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
        if (localDate.isEqual(LocalDate.now()) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
