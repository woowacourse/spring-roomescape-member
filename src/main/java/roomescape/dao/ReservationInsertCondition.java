package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

public class ReservationInsertCondition {

    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public ReservationInsertCondition(String name, LocalDate date, ReservationTime time, ReservationTheme theme) {
        validateDate(date, time.getStartAt());
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateDate(LocalDate date, LocalTime time) {
        LocalDateTime input = LocalDateTime.of(date, time);
        LocalDateTime now = LocalDateTime.now();

        if (input.isBefore(now)) {
            throw new IllegalArgumentException("이전 시간에 대한 예약은 불가능합니다.");
        }
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

    public ReservationTheme getTheme() {
        return theme;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }
}
