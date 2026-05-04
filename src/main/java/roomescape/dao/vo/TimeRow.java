package roomescape.dao.vo;

import roomescape.domain.Time;

import java.time.LocalTime;

public class TimeRow {
    private final Long id;
    private final LocalTime time;

    public TimeRow(Long id, LocalTime time) {
        this.id = id;
        this.time = time;
    }

    public Time toTime() {
        return new Time(id, time);
    }
}
