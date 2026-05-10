package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.Time;

@Repository
public class ReservationDao {
    private static final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        return new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDate("date").toLocalDate(),
                new Time(rs.getLong("time_id"),
                        rs.getTime("start_at").toLocalTime()),
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
        String sql =
                "select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as start_at, r.theme_id as theme_id "
                        + "from reservation r "
                        + "inner join reservation_time t "
                        + "on r.time_id = t.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Reservation> selectByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql =
                """
                SELECT * from reservation where theme_id = ? and date = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, themeId, date);
    }

    public Reservation insert(Reservation reservation) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getThemeId());

        Long id = (long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getThemeId());
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
