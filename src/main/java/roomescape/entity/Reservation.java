package roomescape.entity;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validateTime(time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("존재하지 않는 시간입니다.");
        }
    }

    public static Reservation beforeSave(final String name, final LocalDate date, final ReservationTime time) {
        return new Reservation(null, name, date, time);
    }

    public static Reservation afterSave(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
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
}
