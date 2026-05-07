package roomescape.domain;

import lombok.Getter;
import roomescape.exception.InvalidRequestException;

import java.time.LocalTime;
import java.util.Objects;

@Getter
public class ReservationTime {
    private static final String INVALID_RESERVATION_TIME_MESSAGE = "예약 시간은 비어 있을 수 없습니다.";
    private static final String INVALID_RESERVATION_TIME_ID_MESSAGE = "예약 시간 id는 비어 있을 수 없습니다.";
    private static final String RESERVATION_TIME_ALREADY_HAS_ID_MESSAGE = "이미 id가 존재하는 예약 시간입니다.";

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime withId(Long id) {
        validateId(id);

        if (this.id != null) {
            throw new InvalidRequestException(RESERVATION_TIME_ALREADY_HAS_ID_MESSAGE);
        }

        return new ReservationTime(id, startAt);
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidRequestException(INVALID_RESERVATION_TIME_MESSAGE);
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new InvalidRequestException(INVALID_RESERVATION_TIME_ID_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationTime that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
