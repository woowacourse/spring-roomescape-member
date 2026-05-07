package roomescape.reservation.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ThemeSimpleResponse;
import roomescape.reservation.dto.response.TimeResponse;

@Component
public class ReservationDAO {

    private JdbcTemplate jdbcTemplate;

    public ReservationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation insert(String name, LocalDate date, Long timeId, Long themeId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        ReservationTime time = jdbcTemplate.queryForObject(
                "select id, start_at from reservation_time where id = ?",
                (resultSet, rowNum) -> ReservationTime.of(resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at"))),
                timeId
        );

        return Reservation.of(keyHolder.getKey().longValue(), name, date, time);
    }

    public List<ReservationResponse> findAll() {
        String sql = "select r.id, r.name, r.date, "
                + "t.id as time_id, t.start_at, "
                + "th.id as theme_id, th.name as theme_name "
                + "from reservation r "
                + "inner join reservation_time t on r.time_id = t.id "
                + "inner join theme th on r.theme_id = th.id";

        RowMapper<ReservationResponse> rowMapper = (resultSet, rowNum) -> ReservationResponse.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("date")),
                new TimeResponse(resultSet.getLong("time_id"), LocalTime.parse(resultSet.getString("start_at"))),
                new ThemeSimpleResponse(resultSet.getLong("theme_id"), resultSet.getString("theme_name"))
        );

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);

        return count != null && count > 0;
    }
}
