package roomescape.domain.builder.builderimpl;

import roomescape.domain.ReservationTime;
import roomescape.domain.builder.ReservationTimeBuilder;

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
    public ReservationTimeBuilder startAt(final String time) {
        this.time = LocalTime.parse(time);
        return this;
    }

    @Override
    public ReservationTimeBuilder startAt(final LocalTime time) {
        this.time = time;
        return this;
    }

    @Override
    public ReservationTime build() {
        return new ReservationTime(timeId, time);
    }
}
