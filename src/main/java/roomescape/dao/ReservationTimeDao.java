package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public ReservationTime findById(Long id) {
        try {
            String sql = "SELECT id, start_at from reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReservationTimeNotFoundException("해당 시간을 찾을 수 없습니다.");
        }
    }

    public List<ReservationTime> findAllByIds(List<Long> timeIds) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id IN (:timeIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("timeIds", timeIds);
        return namedParameterJdbcTemplate.query(sql, parameters, reservationTimeRowMapper);
    }

    public List<ReservationTime> findAllReservationTimes() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public Long insertReservationTime(LocalTime time) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, time.toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        try {
            return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new ReservationTimeInUseException("해당 시간에 예약이 존재합니다.");
        }
    }
}
