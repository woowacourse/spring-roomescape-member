package roomescape.time.domain;

import java.time.LocalTime;

public record Time(Long id, LocalTime startAt) {

    public boolean isBefore(LocalTime targetTime) {
        return startAt.isBefore(targetTime);
    }

}
