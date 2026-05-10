package roomescape.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTimeRequest;

@Repository
public class ReservationTimeUpdatingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationTimeUpdatingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(ReservationTimeRequest reservationTimeReq) {
        String sql = "insert into reservation_time(start_at) values (:start_at)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("start_at", reservationTimeReq.getStartAt());

        jdbcTemplate.update(sql, param, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    public void update(Long id, ReservationTimeRequest reservationTimeReq) {
        String sql = "update reservation_time SET start_at = :start_at where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("start_at", reservationTimeReq.getStartAt())
                .addValue("id", id);
        jdbcTemplate.update(sql, param);
    }

    public int delete(Long id) {
        String sql = "delete from reservation_time where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.update(sql, param);
    }
}
