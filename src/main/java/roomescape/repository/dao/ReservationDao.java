package roomescape.repository.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private static final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("start_at", LocalTime.class)
        );
        Theme theme = new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("image_url")
        );
        return new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                reservationTime,
                theme
        );
    };
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> selectAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id AS time_id,
                    rt.start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.image_url
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                WHERE t.is_deleted = FALSE
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Long insert(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation where id = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
