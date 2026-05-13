package roomescape.reservation.domain;

import roomescape.global.exception.DomainNotValidValueException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateReservationName(name);
        validateReservationDate(date);
        validateReservationTime(time);
        validateTheme(theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new DomainNotValidValueException("예약 테마는 비어있을 수 없습니다.");
        }
    }

    private void validateReservationTime(ReservationTime time) {
        if (time == null) {
            throw new DomainNotValidValueException("예약 시간은 비어있을 수 없습니다.");
        }
    }

    private void validateReservationDate(LocalDate date) {
        if (date == null) {
            throw new DomainNotValidValueException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    private static void validateReservationName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainNotValidValueException("예약자 이름은 비어있을 수 없습니다.");
        }
    }
}
