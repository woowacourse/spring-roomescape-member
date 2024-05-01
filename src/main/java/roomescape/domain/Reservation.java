package roomescape.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private final Name name; //TODO 어떻게 생성할지
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(String name, String date, ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation(Long id, String name, String date, ReservationTime time) {
        this(id, name, LocalDate.parse(date), time);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this(id, new Name(name), date, time);
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time) {
        validateDateTime(date, time.getStartAt());
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, date, time);
    }

    public boolean hasSameTimeId(Long timeId) {
        return time.hasSameId(timeId);
    }

    public void validateDateTime(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now(Clock.systemDefaultZone()))) {
            throw new IllegalArgumentException("예약할 수 없는 날짜입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
