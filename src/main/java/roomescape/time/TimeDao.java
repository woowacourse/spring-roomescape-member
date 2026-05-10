package roomescape.time;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class TimeDao {
    private static final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> {
        return new ReservationTime(rs.getLong("id")
                , rs.getTime("start_at").toLocalTime());
    };

    private static final RowMapper<AvailableTime> availableTimeRowMapper = (rs, rowNum) -> {
        return new AvailableTime(
                rs.getTime("start_at").toLocalTime(),
                rs.getBoolean("is_available")
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> selectAll() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public ReservationTime selectById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, timeRowMapper, id);
    }

    public List<AvailableTime> selectByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = "select t.start_at, "
                + " CASE "
                + "WHEN r.id IS NULL THEN true "
                + "ELSE false "
                + "END AS is_available "
                + "FROM reservation_time t "
                + "LEFT JOIN reservation r "
                + "ON t.id = r.time_id "
                + "AND r.theme_id = ? "
                + "AND r.date = ?";

        return jdbcTemplate.query(sql, availableTimeRowMapper, themeId, date);
    }

    public ReservationTime insert(ReservationTime time) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", time.getStartAt());

        Long id = (long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new ReservationTime(id, time.getStartAt());
    }

    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
