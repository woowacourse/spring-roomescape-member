package roomescape.dto.time;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.domain.time.Time;

import java.time.LocalTime;

public record TimeRequest(@NotNull @DateTimeFormat(pattern = "kk:mm") LocalTime startAt) {

    public Time toTime() {
        return new Time(this.startAt);
    }
}
