package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;

@Repository
public class ReservationTimeDao {
    private static final RowMapper<ReservationTime> MAPPER = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime());
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation_time", MAPPER);
    }

    public List<ReservationTimeWithBookStatusResponse> findAllWithBookStatus(LocalDate bookedDate,
                                                                             Long bookedThemeId) {
        String sql = """
                SELECT
                    t.id,
                    t.start_at,
                    EXISTS
                    (SELECT 1 FROM reservation AS r
                    WHERE r.time_id = t.id
                    AND r.date = ?
                    AND r.theme_id = ?) AS booked
                FROM reservation_time AS t
                """;
        RowMapper<ReservationTimeWithBookStatusResponse> mapper = (rs, rowNum) ->
                ReservationTimeWithBookStatusResponse.fromReservationTime(
                        new ReservationTime(rs.getLong("id"),
                                rs.getTime("start_at").toLocalTime()),
                        rs.getBoolean("booked"));
        return jdbcTemplate.query(sql, mapper, bookedDate, bookedThemeId);
    }

    public ReservationTime findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM reservation_time WHERE id = ?", MAPPER, id);
    }

    public boolean existByStartAt(LocalTime startAt) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT * FROM reservation_time WHERE start_at = ?)",
                Boolean.class, startAt);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new ReservationTime(id, reservationTime);
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id) > 0;
    }
}
