package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationTimeDao implements ReservationTimeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public H2ReservationTimeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (:startAt)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource("startAt", reservationTime.getStartAt()), keyHolder);

        Number key = keyHolder.getKey();
        return new ReservationTime(key.longValue(), reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, getReservationTimeRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = :id";

        List<ReservationTime> findReservationTime = jdbcTemplate.query(
            sql, new MapSqlParameterSource("id", id), getReservationTimeRowMapper());
        return findReservationTime.stream().findFirst();
    }

    @Override
    public boolean isExistByTime(LocalTime time) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = :start_at)";

        return Boolean.TRUE == jdbcTemplate.queryForObject(
            sql, new MapSqlParameterSource("start_at", time), Boolean.class);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getObject("start_at", LocalTime.class)
        );
    }
}
