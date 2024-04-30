package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;

    public Reservation(Long id, Name name, ReservationDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation from(Long id, String name, String date, ReservationTime time) {
        return new Reservation(id, new Name(name), ReservationDate.from(date), time);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameAsString() {
        return name.asString();
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public String getDateAndTimeFormat() {
        return this.date.asString() + " " + this.time.getStartAtAsString();
    }

    public boolean isBefore(LocalDate reservationDate, LocalTime reservationTime) {
        if (this.date.isBefore(reservationDate)) {
            return true;
        }
        if (this.date.equals(reservationDate)) {
            return this.time.isBefore(reservationTime);
        }
        return false;
    }
}
