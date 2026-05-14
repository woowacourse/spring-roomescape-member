package roomescape.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;

@Repository
public class ReservationUpdatingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationUpdatingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ReservationCreateRequest reservationReq) {
        String sql = "insert into reservation(name, date, time_id, theme_id) values(:name, :date, :time_id, :theme_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservationReq.getName())
                .addValue("date", reservationReq.getDate())
                .addValue("time_id", reservationReq.getTimeId())
                .addValue("theme_id", reservationReq.getThemeId());

        jdbcTemplate.update(sql, param, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    public void update(Long id, ReservationUpdateRequest reservationReq) {
        String sql = "update reservation SET name = :name, date = :date, time_id = :time_id, theme_id = :theme_id where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservationReq.getName())
                .addValue("date", reservationReq.getDate())
                .addValue("time_id", reservationReq.getTimeId())
                .addValue("theme_id", reservationReq.getThemeId())
                .addValue("id", id);

        jdbcTemplate.update(sql, param);
    }

    public int delete(Long id) {
        String sql = "delete from reservation where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.update(sql, param);
    }
}
