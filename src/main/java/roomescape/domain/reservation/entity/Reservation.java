package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.entity.User;

@Getter
public class Reservation {

    private final Long id;
    private final User user;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    @Builder
    public Reservation(final Long id, final User user, final LocalDate reservationDate,
                       final ReservationTime reservationTime, final Theme theme) {
        this.id = id;
        this.user = user;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.theme = theme;
        validateReservation();
    }

    public static Reservation withoutId(final User user, final LocalDate reservationDate,
                                        final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(null, user, reservationDate, reservationTime, theme);
    }

    private void validateReservation() {
        if (user == null || theme == null) {
            throw new InvalidArgumentException("Reservation field cannot be null");
        }

        validateReservationDateTime();
    }

    private void validateReservationDateTime() {
        if (reservationDate == null || reservationTime == null) {
            throw new InvalidArgumentException("예약 날짜와 시간이 null일 수 없습니다.");
        }
    }

    public void validateNotPastReservation(final LocalDateTime now) {
        if (isPast(now)) {
            throw new InvalidArgumentException("이미 지난 예약 시간입니다.");
        }
    }

    private boolean isPast(final LocalDateTime now) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, getReservationStartTime());
        return reservationDateTime.isBefore(now);
    }

    public LocalTime getReservationStartTime() {
        return reservationTime.getStartAt();
    }

    public boolean existId() {
        return id != null;
    }

    public Long getReservationTimeId() {
        return reservationTime.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public String getName() {
        return user.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }
}
