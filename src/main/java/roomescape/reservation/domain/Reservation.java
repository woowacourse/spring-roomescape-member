package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.ExceptionCode;

public class Reservation {

    private final Long id;
    private final UserName userName;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, UserName userName, LocalDate date, ReservationTime time, Theme theme) {
        validateDateTime(date, time);

        this.id = id;
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this(id, new UserName(name), date, time, theme);
    }

    private void validateDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (LocalDateTime.now().isAfter(reservationDateTime)) {
            throw new CustomException(ExceptionCode.PAST_TIME_SLOT_RESERVATION);
        }
    }

    public boolean isSameDate(LocalDate localDate) {
        return date.isEqual(localDate);
    }

    public boolean isSameTime(ReservationTime reservationTime) {
        return time.equals(reservationTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return userName.getValue();
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
