package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationTime;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER =
            (resultSet, rowNum) -> ReservationTime.afterSave(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime()
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        final String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);
        long generatedId = keyHolder.getKey().longValue();

        return ReservationTime.afterSave(
                generatedId,
                reservationTime.getStartAt()
        );
    }

    @Override
    public ReservationTime findById(long timeId) {
        try {
            final String sql = "SELECT * FROM reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, timeId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> ReservationTime.afterSave(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()
                ));
    }

    @Override
    public boolean existByTime(LocalTime createTime) {
        final String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, createTime);
        return count != null && count > 0;
    }

    @Override
    public List<ReservationTime> getAvailableReservationTimeOf(LocalDate date, long themeId) {
        final String sql = """
                SELECT *
                FROM reservation_time
                WHERE id NOT IN (
                    SELECT rt.id
                    FROM reservation_time AS rt
                    INNER JOIN reservation AS r
                    ON r.time_id = rt.id
                    WHERE r.date = ?
                    AND r.theme_id = ?
                )
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }

    @Override
    public boolean deleteById(long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

}
