package roomescape.dto.time;

import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import roomescape.domain.time.Time;

public record TimeRequest(@NonNull @DateTimeFormat(pattern = "kk:mm") LocalTime startAt) {

    public Time toTime() {
        return new Time(this.startAt);
    }
}
