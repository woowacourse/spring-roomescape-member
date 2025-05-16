package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.custom.InvalidInputException;

public class ReservationTime {

    private static final int NON_SAVED_STATUS = 0;

    private final long id;
    private final LocalTime startAt;

    public ReservationTime(final long id, final LocalTime startAt) {
        validateInvalidInput(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(NON_SAVED_STATUS, startAt);
    }

    public ReservationTime(final long id, final ReservationTime savedReservationTime) {
        this(id, savedReservationTime.getStartAt());
    }

    private void validateInvalidInput(final LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("시간은 빈 값이 입력될 수 없습니다");
        }
    }

    public boolean isPastTime() {
        final LocalTime currentTime = LocalTime.now();

        return startAt.isBefore(currentTime);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return getId() == that.getId() && Objects.equals(getStartAt(), that.getStartAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartAt());
    }
}
