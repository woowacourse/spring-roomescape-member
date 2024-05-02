package roomescape.repository.reservationtime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeDao implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    private static RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(reservationTime);
        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, getReservationTimeRowMapper());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            String sql = "SELECT * FROM reservation_time WHERE id = ?";
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, getReservationTimeRowMapper(), id);
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
