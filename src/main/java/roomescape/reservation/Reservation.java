package roomescape.reservation;

import java.time.LocalDate;
import roomescape.exception.InvalidStateException;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

public class Reservation {

    private Long id;
    private final String userName;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(String userName, Theme theme, LocalDate date, ReservationTime time) {
        this(null, userName, theme, date, time);
    }

    public Reservation(Long id, String userName, Theme theme, LocalDate date, ReservationTime time) {
        validate(userName, theme, date, time);
        this.id = id;
        this.userName = userName;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    private void validate(String userName, Theme theme, LocalDate date, ReservationTime time) {
        validateUserName(userName);
        validateTheme(theme);
        validateDate(date);
        validateTime(time);
    }

    private void validateUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new InvalidStateException("예약자 이름은 비어있을 수 없습니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new InvalidStateException("예약 테마는 비어있을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidStateException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new InvalidStateException("예약 시간은 비어있을 수 없습니다.");
        }
    }
}
