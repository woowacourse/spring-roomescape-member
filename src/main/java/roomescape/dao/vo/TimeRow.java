package roomescape.dao.vo;

import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Time;

import java.time.LocalTime;

public class TimeRow {
    public static final RowMapper<TimeRow> ROW_MAPPER = (resultSet, rowNum) -> {
        TimeRow row = new TimeRow(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
        return row;
    };
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
