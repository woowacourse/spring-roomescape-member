package roomescape.user.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.user.dto.AvailableTimeResponse;

@Repository
public class ReservationTimeDao {
    private static final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> {
        return new ReservationTime(rs.getLong("id")
                , LocalTime.parse(rs.getString("start_at"), DateTimeFormatter.ofPattern("HH:mm")));
    };

    private static final RowMapper<AvailableTimeResponse> availableTimeRowMapper = (rs, rowNum) -> {
        return new AvailableTimeResponse(
                LocalTime.parse(rs.getString("start_at")),
                rs.getBoolean("is_available")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> selectAll() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public ReservationTime selectById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, timeRowMapper, id);
    }

    public List<AvailableTimeResponse> selectByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                select t.start_at,
                    CASE
                        WHEN r.id IS NULL THEN true
                        ELSE false
                    END AS is_available
                FROM reservation_time t
                LEFT JOIN reservation r
                ON t.id = r.time_id
                AND r.theme_id = ?
                AND r.date = ?
                """;

        return jdbcTemplate.query(sql, availableTimeRowMapper, themeId, date);
    }
}
