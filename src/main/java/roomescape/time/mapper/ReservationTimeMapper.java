package roomescape.time.mapper;

import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.entity.ReservationTimeEntity;

public class ReservationTimeMapper {

    private ReservationTimeMapper() {
    }

    public static ReservationTime toReservationTime(ReservationTimeEntity from) {
        return new ReservationTime(from.getId(), from.getStartAt());
    }
}
