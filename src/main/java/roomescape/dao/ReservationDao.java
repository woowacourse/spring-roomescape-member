package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String selectSQL = """
                SELECT 
                    r.id AS reservation_id, 
                    r.name AS reservation_name, 
                    r.date AS reservation_date,
                    t.id AS time_id, 
                    t.start_at AS time_value,
                    th.id AS theme_id, 
                    th.name AS theme_name, 
                    th.description AS theme_description, 
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                """;

        RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                new Name(rs.getString("reservation_name")),
                rs.getDate("reservation_date").toLocalDate(),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getTime("time_value").toLocalTime()
                ),
                new RoomTheme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_description"),
                        rs.getString("theme_thumbnail")
                )
        );

        return jdbcTemplate.query(selectSQL, rowMapper);
    }

    public boolean existsByDateTime(LocalDate date, Long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT * FROM reservation WHERE date = ? AND time_id = ?)",
                Boolean.class, date, timeId);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return reservation.withId(id);
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) > 0;
    }
}
