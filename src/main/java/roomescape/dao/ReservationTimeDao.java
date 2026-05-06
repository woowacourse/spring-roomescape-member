package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTime findById(Long id) {
        String sql = "SELECT id, start_at from reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getReservationTimeRowMapper(), id);
    }

    public List<ReservationTime> findAllReservationTimes() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, getReservationTimeRowMapper());
    }

    public Long insertReservationTime(LocalTime time) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, time.toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    public Map<ReservationTime, Boolean> findAvailableTimes(LocalDate date, Long id) {
        String sql = """
                SELECT
                    rt.id AS time_id,
                    rt.start_at,
                    NOT EXISTS (
                        SELECT 1
                        FROM reservation r
                        WHERE r.time_id = rt.id
                          AND r.theme_id = ?
                          AND r.date = ?
                    ) AS available
                FROM reservation_time rt;
                """;
        return jdbcTemplate.query(sql, getMapResultSetExtractor(), id, date);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }

    private ResultSetExtractor<Map<ReservationTime, Boolean>> getMapResultSetExtractor() {
        return (ResultSet rs) -> {
            Map<ReservationTime, Boolean> results = new LinkedHashMap<>();

            while (rs.next()) {
                ReservationTime reservationTime = new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getObject("start_at", LocalTime.class)
                );
                boolean isAvailable = rs.getBoolean("available");
                results.put(reservationTime, isAvailable);
            }
            return results;
        };
    }
}
