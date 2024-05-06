package roomescape.domain;

import roomescape.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        checkNull(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void checkNull(String name, LocalDate date, ReservationTime time, Theme theme) {
        try {
            Objects.requireNonNull(name, "[ERROR] 이름은 null일 수 없습니다.");
            Objects.requireNonNull(date, "[ERROR] 날짜는 null일 수 없습니다.");
            Objects.requireNonNull(time, "[ERROR] 시간은 null일 수 없습니다.");
            Objects.requireNonNull(theme, "[ERROR] 테마는 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
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

    public Theme getTheme() {
        return theme;
    }

    public Long getReservationTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }
}
