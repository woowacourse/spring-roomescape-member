package roomescape.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateMaxLength(name);
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.date = Objects.requireNonNull(date);
        this.time = Objects.requireNonNull(time);
        this.theme = Objects.requireNonNull(theme);
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation copyWithId(Long id) {
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
            throw new IllegalArgumentException("name의 최대 제한 길이를 초과했습니다.");
        }
    }

    public Long getId() {
        if (id == null) {
            throw new NullPointerException("id값이 존재하지 않습니다.");
        }
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

    public Theme getTheme() {
        return theme;
    }
}
