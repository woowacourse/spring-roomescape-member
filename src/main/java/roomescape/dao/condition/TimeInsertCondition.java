package roomescape.dao.condition;

import java.time.LocalTime;

public class TimeInsertCondition {

    private final LocalTime startAt;

    public TimeInsertCondition(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
