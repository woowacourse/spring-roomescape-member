package roomescape.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Name name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
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

    private void validateDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (LocalDateTime.of(date, time).isBefore(now)) {
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
