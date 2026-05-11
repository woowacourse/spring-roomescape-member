package roomescape.reservation.dao;

import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.response.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeFindAllResponse;

@Component
public class ReservationTimeDAO {

    private JdbcTemplate jdbcTemplate;

    public ReservationTimeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(ReservationTime reservationTime) {
        jdbcTemplate.update("insert into reservation_time (start_at) values (?)", reservationTime.getStartAt());

        Long id = jdbcTemplate.queryForObject("select t.id from reservation_time t where t.start_at = ?", Long.class,
                reservationTime.getStartAt());

        return id;
    }

    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";
        RowMapper<ReservationTime> rowMapper = (resultSet, rowNum) -> ReservationTime.of(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );

        return jdbcTemplate.query(sql, rowMapper);
    }

    public ReservationTime findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select id, start_at from reservation_time where id = ?",
                    (resultSet, rowNum) -> ReservationTime.of(resultSet.getLong("id"),
                            LocalTime.parse(resultSet.getString("start_at"))),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다.");
        }
    }

    public void delete(Long id) {
        jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }
}

