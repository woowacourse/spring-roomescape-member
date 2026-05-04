package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationRequest;

import java.sql.PreparedStatement;

@Repository
public class ReservationUpdatingDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationUpdatingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long id, ReservationRequest reservationReq) {
        String sql = "update reservation SET name = ?, date = ?, time_id = ?, theme_id = ? where id = ?";
        jdbcTemplate.update(sql, reservationReq.getName(), reservationReq.getDate(), reservationReq.getTimeId(), reservationReq.getThemeId(), id);
    }

    public int delete(Long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Long insert(ReservationRequest reservationReq) {
        String sql = "insert into reservation(name, date, time_id, theme_id) values(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, reservationReq.getName());
            ps.setObject(2, reservationReq.getDate());
            ps.setLong(3, reservationReq.getTimeId());
            ps.setLong(4, reservationReq.getThemeId());
            return ps;
        }, keyHolder);

        Long id =  keyHolder.getKey().longValue();

        return id;
    }
}
