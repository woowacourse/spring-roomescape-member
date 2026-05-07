package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id AS r_id, 
                    r.name, 
                    r.date, 
                    t.id AS t_id, 
                    t.start_at,
                    theme.id AS theme_id,
                    theme.name AS theme_name,
                    theme.description AS theme_description,
                    theme.thumbnail_url AS theme_thumbnail_url
                FROM 
                    reservation r 
                        INNER JOIN 
                        time t 
                            INNER JOIN 
                        theme theme
                            ON r.time_id = t.id 
                                   AND r.theme_id = theme.id
                """;
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        String sql = """
                SELECT 
                    r.id AS r_id,
                    r.name,
                    r.date,
                    t.id AS t_id,
                    t.start_at, 
                    theme.id as theme_id,
                    theme.name AS theme_name,
                    theme.description AS theme_description,
                    theme.thumbnail_url AS theme_thumbnail_url
                FROM 
                    reservation r 
                        INNER JOIN 
                        time t 
                        INNER JOIN 
                        theme theme
                            ON 
                                r.time_id = t.id 
                                   AND 
                                r.theme_id = theme.id
                WHERE r.id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), reservationId));
    }

    @Override
    public List<Long> findByThemeIdAndDate(long themeId, LocalDate date) {
        String sql = """
                        SELECT time_id
                        FROM reservation 
                        WHERE theme_id = ? and date = ?
                """;
        return jdbcTemplate.queryForList(sql, Long.class, themeId, date);
    }

    @Override
    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(reservation);
        long reservationId = insert.executeAndReturnKey(params).longValue();
        return new Reservation(
                reservationId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(Reservation reservation) {
        return Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().id(),
                "theme_id", reservation.getTheme().id()
        );
    }

    @Override
    public void deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    private RowMapper<Reservation> rowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("r_id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                new Time(
                        rs.getLong("t_id"),
                        rs.getObject("start_at", LocalTime.class)),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_description"),
                        rs.getString("theme_thumbnail_url")
                )
        );
    }
}
