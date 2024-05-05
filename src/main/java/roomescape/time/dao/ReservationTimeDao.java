package roomescape.time.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationTime> timeRowMapper;
    private final RowMapper<ReservationUserTime> userTimeRowMapper;

    public ReservationTimeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final RowMapper<ReservationTime> timeRowMapper, final RowMapper<ReservationUserTime> userTimeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
        this.timeRowMapper = timeRowMapper;
        this.userTimeRowMapper = userTimeRowMapper;
    }

    public long save(final ReservationTime reservationTime) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt().toString());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<ReservationTime> findById(final long id) {
        final String sql = "select * from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, timeRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        final String sql = "select * from reservation_time order by start_at asc";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public int deleteById(final long id) {
        final String sql = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        final String sql = """
                SELECT t.id, t.start_at, 
                EXISTS (SELECT 1 FROM reservation r WHERE r.time_id = t.id AND r.date = ? AND r.theme_id = ?) 
                AS already_booked 
                FROM reservation_time t
                """;
        return jdbcTemplate.query(sql, userTimeRowMapper, date, themeId);
    }

    public boolean checkExistTime(final ReservationTime reservationTime) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, reservationTime.getStartAt());
        return Boolean.TRUE.equals(result);
    }
}
