package roomescape.domain.builder;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public interface ReservationTimeBuilder {
    ReservationTimeBuilder timeId(long timeId);

    ReservationTimeBuilder startAt(String time);

    ReservationTimeBuilder startAt(LocalTime time);

    ReservationTime build();
}
