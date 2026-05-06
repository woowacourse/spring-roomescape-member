package roomescape.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );

        Reservation reservation = new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("date").toLocalDate(),
                reservationTime,
                theme
        );
        return reservation;
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation insert(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());

        Number generatedId = jdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(
                generatedId.longValue(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public List<Reservation> select() {
        String sql = """
                SELECT r.id, 
                       r.name as reservation_name, 
                       r.date,
                       rt.id as time_id,
                       rt.start_at,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description,
                       t.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id
                INNER JOIN theme AS t 
                ON r.theme_id = t.id""";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public List<Reservation> selectByThemeIdAndDate(long themeId, LocalDate date) {
        String sql = """
                SELECT r.id, 
                       r.name as reservation_name, 
                       r.date,
                       rt.id as time_id,
                       rt.start_at,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description,
                       t.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id
                INNER JOIN theme AS t 
                ON r.theme_id = t.id
                WHERE r.theme_id = ?
                AND r.date = ? 
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, themeId, date);
    }

    public boolean existsById(Long reservationId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE id = ?
                )
                """;

        return jdbcTemplate.queryForObject(sql, boolean.class, reservationId);
    }

    public void delete(Long reservationId) {
        String sql = """
                DELETE FROM reservation
                WHERE id = ?""";
        jdbcTemplate.update(sql, reservationId);
    }
}
