package roomescape.reservation.domain;

import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
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
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateReservationTime(ReservationTime time) {
        if (time == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateReservationDate(LocalDate date) {
        if (date == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private static void validateReservationName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }
}
