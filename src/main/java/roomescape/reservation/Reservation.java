package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

public class Reservation {

    private Long id;
    private final String userName;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;
    private LocalDateTime deletedAt;

    public Reservation(Long id, String userName, Theme theme, LocalDate date, ReservationTime time,
                       LocalDateTime deletedAt) {
        this.id = id;
        this.userName = userName;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.deletedAt = deletedAt;
    }

    public Reservation(String userName, Theme theme, LocalDate date, ReservationTime time, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        validateReservationTime(reservationDateTime, now);
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    private void validateReservationTime(LocalDateTime reservationDateTime, LocalDateTime now) {
        if (reservationDateTime.isBefore(now)) {
            throw new RoomescapeException(ErrorCode.PAST_RESERVATION);
        }
    }
}
