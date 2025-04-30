package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationTime;
import roomescape.exceptions.EntityNotFoundException;

import java.sql.Time;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationTimeJDBCDao implements ReservationTimeRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReservationTimeJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public ReservationTime findById(Long id) {
        String sql = "select * from reservation_time where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedJdbcTemplate.queryForObject(sql, params, ReservationTime.class);
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into  reservation_time (start_at) values (:startAt)";
        MapSqlParameterSource params = new MapSqlParameterSource("startAt", Time.valueOf(reservationTime.startAt()));

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new ReservationTime(id, reservationTime.startAt());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation_time where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);

        if (result == 0) {
            throw new EntityNotFoundException("예약 데이터를 찾을 수 없습니다:" + id);
        }
    }

    private RowMapper<ReservationTime> getReservationRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
