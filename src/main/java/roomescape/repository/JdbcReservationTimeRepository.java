package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"), LocalTime.parse(resultSet.getString("start_at")));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;


    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(ReservationTime reservationTime) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(reservationTime);
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            String sql = "SELECT * FROM reservation_time WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            String sql = "DELETE FROM reservation_time WHERE id = ?";
            jdbcTemplate.update(sql, id);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<ReservationTime> findByTime(final LocalTime time) {
        try {
            String sql = "SELECT * FROM reservation_time WHERE start_at = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, time));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
