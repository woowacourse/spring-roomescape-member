package roomescape.dao.vo;

import roomescape.domain.Time;

import java.util.List;

public class TimeRows {
    private final List<TimeRow> timeRows;

    public TimeRows(List<TimeRow> timeRows) {
        this.timeRows = timeRows;
    }

    public List<Time> toTimes() {
        return timeRows.stream()
                .map(TimeRow::toTime)
                .toList();
    }
}
