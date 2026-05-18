package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.ErrorReason;
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
        validateNotPast(date, time, now);
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

    public Reservation cancel(LocalDateTime now) {
        validateActiveReservation();
        validateNotPast(date, time, now);
        return new Reservation(this.id, this.userName, this.theme, this.date, this.time, now);
    }

    public Reservation change(Theme newTheme, LocalDate newDate, ReservationTime newTime, LocalDateTime now) {
        validateActiveReservation();
        validateNotPast(newDate, newTime, now);
        return new Reservation(this.id, this.userName, newTheme, newDate, newTime, null);
    }

    public void checkOwner(String userName) {
        if (!belongsTo(userName)) {
            throw new RoomescapeException(ErrorReason.RESERVATION_NOT_OWNER);
        }
    }

    private boolean belongsTo(String userName) {
        return Objects.equals(this.userName, userName);
    }

    private void validateActiveReservation() {
        if (this.deletedAt != null) {
            throw new RoomescapeException(ErrorReason.RESERVATION_ALREADY_CANCELED);
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time, LocalDateTime now) {
        if (time.isPast(date, now)) {
            throw new RoomescapeException(ErrorReason.PAST_RESERVATION);
        }
    }
}
