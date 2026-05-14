package roomescape.domain;

import java.util.List;
import java.util.Set;
import roomescape.global.exception.reservation.DuplicateReservationException;

public class ReservedTimes {

    private final Set<Long> reservedTimeIds;

    public ReservedTimes(List<Long> reservedTimeIds) {
        this.reservedTimeIds = Set.copyOf(reservedTimeIds);
    }

    public void validateAvailable(Long timeId) {
        if (isReserved(timeId)) {
            throw new DuplicateReservationException("해당 시간과 테마에는 이미 예약이 존재합니다.");
        }
    }

    public boolean isAvailable(Long timeId) {
        return !reservedTimeIds.contains(timeId);
    }

    public boolean isReserved(Long timeId) {
        return reservedTimeIds.contains(timeId);
    }
}
