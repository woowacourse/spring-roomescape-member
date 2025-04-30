package roomescape.domain_entity;

import java.time.LocalDate;

public class Reservation {
    private Id id;
    private String name;
    private LocalDate date;
    private ReservationTime time;

    public Reservation() {
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(Id.empty(), name, date, time);
    }

    public Reservation(Id id, String name, LocalDate date, ReservationTime time) {
        validateMaxLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation copyWithId(Id id) {
        return new Reservation(id, name, date, time);
    }

    public void validatePastDateTime() {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now)) {
            throw new IllegalArgumentException("지난 날짜와 시간의 예약은 생성 불가능합니다.");
        }
    }

    private void validateMaxLength(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("요청 필드가 최대 제한 길이를 초과했습니다.");
        }
    }

    public long getId() {
        return id.value();
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
