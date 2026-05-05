package roomescape.dao.vo;

import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.Time;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRow {
    public static final RowMapper<ReservationRow> ROW_MAPPER = (rs, rowNum) ->
            new ReservationRow(
                    rs.getLong("id"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getLong("time_id"),
                    LocalTime.parse(rs.getString("start_at"))
            );
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;
    private final LocalTime startAt;

    public ReservationRow(Long id, String name, LocalDate date, Long timeId, LocalTime startAt) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.startAt = startAt;
    }

    public Reservation toReservation() {
        return new Reservation(
                id,
                name,
                date,
                new Time(timeId, startAt)
        );
    }
}
