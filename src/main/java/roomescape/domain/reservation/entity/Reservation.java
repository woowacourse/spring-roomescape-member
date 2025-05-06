package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;

@Getter
public class Reservation {

    private static final int MAX_NAME_LENGTH = 25;
    private final Long id;
    private final String name;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    @Builder
    public Reservation(final Long id, final String name, final LocalDate reservationDate,
                       final ReservationTime reservationTime, final Theme theme) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.theme = theme;
        validateReservation();
    }

    public static Reservation withoutId(final String name, final LocalDate reservationDate,
                                        final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(null, name, reservationDate, reservationTime, theme);
    }

    private void validateReservation() {
        if (name == null || reservationDate == null || reservationTime == null || theme == null) {
            throw new InvalidArgumentException("Reservation field cannot be null");
        }
        validateName();
    }

    private void validateName() {
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidArgumentException("invalid reservation name");
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

    public boolean existId() {
        return id != null;
    }

    public Long getReservationTimeId() {
        return reservationTime.getId();
    }

    public LocalTime getReservationStartTime() {
        return reservationTime.getStartAt();
    }

    public Long getThemeId() {
        return theme.getId();
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
