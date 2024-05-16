package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exceptions.queryResultSizeException;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(ReservationTime reservationTime) {
        LocalTime startAt = reservationTime.getStartAt();

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("start_at", startAt);

        long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new ReservationTime(id, startAt);
    }

    public ReservationTime findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        ReservationTime reservationTime;
        try {
            reservationTime = jdbcTemplate.queryForObject(sql, reservationTimeRowMapper(), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }

        if (reservationTime == null) {
            throw new EmptyResultDataAccessException("id에 맞는 예약시간이 존재하지 않습니다.", 1);
        }
        return reservationTime;
    }

    public List<ReservationTime> selectAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
