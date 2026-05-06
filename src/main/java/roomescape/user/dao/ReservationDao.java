package roomescape.user.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {
    private static final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("date")),
                new ReservationTime(rs.getLong("time_id"),
                        LocalTime.parse(rs.getString("start_at"), DateTimeFormatter.ofPattern("HH:mm"))),
                rs.getLong("theme_id")
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> selectAll() {
        String sql = "select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as start_at, r.theme_id as theme_id "
                + "from reservation r "
                + "inner join reservation_time t "
                + "on r.time_id = t.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Reservation insert(Reservation reservation) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());

        Long id = (long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getThemeId());
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
