package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.IllegalDateException;
import roomescape.exception.IllegalTimeException;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약자 이름입니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalDateException("[ERROR] 유효하지 않은 날짜입니다.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalDateException("[ERROR] 이미 지난 날짜입니다.");
        }
    }

    private void validateTime(LocalDate date, ReservationTime time) {
        if (LocalDate.now().isEqual(date) && time.inPast()) {
            throw new IllegalTimeException("[ERROR] 이미 지난 시간입니다.");
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

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }
}
