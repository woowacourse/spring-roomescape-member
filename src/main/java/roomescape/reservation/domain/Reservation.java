package roomescape.reservation.domain;

import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

public class Reservation {

    private static final int MAX_NAME_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation create(String name, LocalDate date, ReservationTime time) {
        validateNameSize(name);
        return new Reservation(null, name, date, time);
    }

    private static void validateNameSize(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 10글자 이하여야 합니다. (현재 이름의 글자 수: %d)", name.length()));
        }
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
