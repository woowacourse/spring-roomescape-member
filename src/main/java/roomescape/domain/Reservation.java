package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Member member,
                       final ReservationDate date,
                       final ReservationTime time,
                       final Theme theme) {
        this.id = null;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final Long id,
                       final Member member,
                       final ReservationDate date,
                       final ReservationTime time,
                       final Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(final Long id, final Reservation reservation) {
        return new Reservation(id, reservation.member, reservation.date, reservation.time, reservation.theme);
    }

    public static Reservation of(final Long id,
                                 final Member member,
                                 final String date,
                                 final ReservationTime time,
                                 final Theme theme) {
        return new Reservation(id, member, ReservationDate.from(date), time, theme);
    }

//    public static Reservation from(final Long id, final String name, final String date, final ReservationTime time,
//                                   final Theme theme) {
//        return new Reservation(id, new Name(name), ReservationDate.from(date), time, theme);
//    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

//    public String getNameAsString() {
//        return member.asString();
//    }

    public ReservationDate getDate() {
        return date;
    }

    public String getDateAsString() {
        return date.asString();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getDateAndTimeFormat() {
        return this.date.asString() + " " + this.time.getStartAtAsString();
    }

    public boolean isBefore(final LocalDate localDate, final LocalTime localTime) {
        if (this.date.isBefore(localDate)) {
            return true;
        }
        if (this.date.isEqual(localDate)) {
            return this.time.isBefore(localTime);
        }
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
