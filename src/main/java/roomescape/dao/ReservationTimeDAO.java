package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationTimeDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeDAO(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(final ReservationTime reservationTime) {
        final LocalTime startAt = reservationTime.getStartAt();

        final SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("start_at", startAt);

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new ReservationTime(id, startAt);
    }

    public ReservationTime findById(final Long id) {
        final String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper(), id);
    }

    public List<ReservationTime> selectAll() {
        final String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean existReservationTimeOf(final LocalTime time) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM reservation_time
                    WHERE start_at = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, time);
    }

    public List<AvailableTimeResponse> findAvailableTimes(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT
                    t.start_at,
                    t.id,
                    CASE r.id WHEN IS NULL THEN false ELSE true END AS already_booked
                FROM reservation_time AS t
                LEFT JOIN reservation AS r
                ON t.id = r.time_id
                AND r.date = ? AND r.theme_id = ?
                ORDER BY t.start_at;
                """;
        return jdbcTemplate.query(sql, availableTimeResponseRowMapper(), date, themeId);
    }

    private RowMapper<AvailableTimeResponse> availableTimeResponseRowMapper() {
        return (resultSet, rowNum) -> new AvailableTimeResponse(
                resultSet.getTime("start_at").toLocalTime(),
                resultSet.getLong("id"),
                resultSet.getBoolean("already_booked")
        );
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(resultSet.getLong("id"), resultSet.getTime("start_at").toLocalTime());
    }
}
