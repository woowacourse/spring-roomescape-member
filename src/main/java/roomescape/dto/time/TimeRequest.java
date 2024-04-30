package roomescape.dto.time;

import java.time.LocalTime;
import roomescape.domain.time.Time;

public record TimeRequest(LocalTime startAt) {

    public Time toTime() {
        return new Time(this.startAt);
    }
}
