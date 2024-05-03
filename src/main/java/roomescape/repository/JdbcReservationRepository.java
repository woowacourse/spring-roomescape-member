package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getTime("time_value").toLocalTime()),
            new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
    );

    private RowMapper<ReservationTime> timeRowMapper = ((rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    ));
    private RowMapper<Theme> themeRowMapper = ((rs, rowNum) -> new Theme(
            rs.getLong("theme_id"),
            rs.getString("theme_name"),
            rs.getString("theme_description"),
            rs.getString("theme_thumbnail")
    ));

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                r.id AS reservation_id, 
                r.name, 
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value, 
                theme.id AS theme_id, 
                theme.name AS theme_name, 
                theme.description AS theme_description, 
                theme.thumbnail AS theme_thumbnail 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id 
                INNER JOIN theme 
                ON r.theme_id = theme.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                r.id AS reservation_id, 
                r.name, 
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value, 
                theme.id AS theme_id, 
                theme.name AS theme_name, 
                theme.description AS theme_description, 
                theme.thumbnail AS theme_thumbnail 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id 
                INNER JOIN theme 
                ON r.theme_id = theme.id 
                WHERE r.id = ?;
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Reservation insert(Reservation reservation) {
        Map<String, Object> reservationRow = new HashMap<>();
        reservationRow.put("name", reservation.getName());
        reservationRow.put("date", reservation.getDate());
        reservationRow.put("time_id", reservation.getTimeId());
        reservationRow.put("theme_id", reservation.getThemeId());
        Long id = simpleJdbcInsert.executeAndReturnKey(reservationRow).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT 1 
                    FROM reservation 
                    WHERE date = ? AND time_id = ? AND theme_id = ?
                );
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<ReservationTime> findByDateAndTheme(LocalDate date, Long themeId) {
        String sql = """
                SELECT t.id, t.start_at 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id 
                WHERE date = ? AND theme_id = ?;
                """;
        return jdbcTemplate.query(sql, timeRowMapper, date, themeId);
    }

    @Override
    public List<Theme> findThemeOrderByReservationCount() {
        String sql = """
                SELECT 
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                COUNT(th.id) AS reservation_count
                FROM reservation AS r
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                WHERE r.date BETWEEN DATEADD('day', -7, CURRENT_DATE()) AND DATEADD('day', -1, CURRENT_DATE())
                GROUP BY th.id
                ORDER BY reservation_count DESC 
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, themeRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
