package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final Long id, final Name name, final LocalDate date, final ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation of(long id, String name, String date, long timeId, String time) {
        LocalDate parsedDate = LocalDate.parse(date);
        LocalTime parsedTime = LocalTime.parse(time);

        return new Reservation(id, new Name(name), parsedDate, new ReservationTime(timeId, parsedTime));
    }

    public static Reservation of(final String name, final LocalDate date, final ReservationTime reservationTime) {
        return new Reservation(null, new Name(name), date, reservationTime);
    }

    public long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public long getTimeId() {
        return time.getId();
    }
}
