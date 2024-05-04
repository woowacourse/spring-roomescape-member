package roomescape.domain.builder;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public class ReservationTimeBuilderImpl implements ReservationTimeBuilder {
    private Long timeId;
    private LocalTime time;

    @Override
    public ReservationTimeBuilder timeId(final long timeId) {
        this.timeId = timeId;
        return this;
    }

    @Override
    public ReservationTimeBuilder startAt(final String startAt) {
        this.time = LocalTime.parse(startAt);
        return this;
    }

    @Override
    public ReservationTime build() {
        return new ReservationTime(timeId, time);
    }
}
