package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import roomescape.domain.ReservationTime;

@Repository
public class TimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> rowMapper;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
    }

    public List<ReservationTime> readTimes() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<ReservationTime> readTimesExistsReservation(String date, Long themeId) {
        String sql = """
                SELECT id, start_at 
                FROM reservation_time
                WHERE id IN (
                    SELECT time_id
                    FROM reservation
                    WHERE date = ? AND theme_id = ?
                )
                """;
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    public Optional<ReservationTime> readTimeById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean existsTimeByStartAt(String startAt) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation_time
                    WHERE start_at = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    public ReservationTime createTime(ReservationTime time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation_time(start_at) VALUES (?)";

        jdbcTemplate.update((connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setObject(1, time.getStartAt());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return time.createWithId(id);
    }

    public void deleteTime(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
