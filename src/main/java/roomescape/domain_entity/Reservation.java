package roomescape.domain_entity;

import java.time.LocalDate;

public class Reservation {
    private final Id id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(Id.empty(), name, date, time, theme);
    }

    public Reservation(Id id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateMaxLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation copyWithId(Id id) {
        return new Reservation(id, name, date, time, theme);
    }

    public void validatePastDateTime() {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now) || (date.equals(now) && time.isPastTime())) {
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

    public Theme getTheme() {
        return theme;
    }
}
