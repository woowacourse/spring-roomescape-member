package roomescape.repository.roomescape;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.roomescape.ReservationTime;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationTime> reservationTimeRowMapper;

    public ReservationTimeDao(
            final DataSource dataSource,
            final RowMapper<ReservationTime> reservationTimeRowMapper
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("ID");
        this.reservationTimeRowMapper = reservationTimeRowMapper;
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(reservationTime);
        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservationTime.assignId(id);
    }

    public List<ReservationTime> getAll() {
        return jdbcTemplate.query(
                "SELECT reservation_time.id AS time_id, start_at FROM reservation_time",
                reservationTimeRowMapper);
    }

    public Optional<ReservationTime> findById(final long id) {
        try {
            String sql = "SELECT reservation_time.id AS time_id, start_at FROM reservation_time WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public int delete(final long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", Long.valueOf(id));
    }
}
