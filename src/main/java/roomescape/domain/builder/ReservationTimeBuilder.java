package roomescape.domain.builder;

import roomescape.domain.ReservationTime;

public interface ReservationTimeBuilder {
    ReservationTimeBuilder timeId(long timeId);

    ReservationTimeBuilder startAt(String startAt);

    ReservationTime build();
}
