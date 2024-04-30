package roomescape.reservationtime.repository;

import java.sql.Time;
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
import roomescape.reservationtime.model.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<ReservationTime> reservationTimeRowMapper =
            (resultSet, rowNum) -> new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime()
            );

    public Long save(final ReservationTime reservationTime) {
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("start_at", Time.valueOf(reservationTime.getTime()));
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    @Override
    public Optional<ReservationTime> findById(final Long timeId) {
        String sql = "select id, start_at from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, timeId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(final Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
