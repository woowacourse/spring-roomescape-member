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
import roomescape.domain.ReservationStatus;
import roomescape.domain.Theme;
import roomescape.domain.Time;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id AS r_id, 
                    r.name, 
                    r.date, 
                    r.status,                
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
                    r.status,
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

        return jdbcTemplate.query(sql, rowMapper(), reservationId).stream().findFirst();
    }

    @Override
    public List<Reservation> findByName(String name) {
        String sql = """
                SELECT 
                    r.id AS r_id,
                    r.name,
                    r.date,
                    r.status,
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
                WHERE r.name = ?
                """;

        return jdbcTemplate.query(sql, rowMapper(), name).stream().toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> params = createParams(reservation);
        Long reservationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(
                reservationId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme(),
                reservation.getReservationStatus()
        );
    }

    private Map<String, Object> createParams(Reservation reservation) {
        return Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId(),
                "status", reservation.getReservationStatus().name()
        );
    }

    @Override
    public void deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    @Override
    public boolean isExistBy(Long themeId, LocalDate date, Long reservationTimeId) {
        String sql = """
                        SELECT EXISTS (
                            SELECT 1
                            FROM reservation 
                            WHERE theme_id = ? 
                            AND date = ?
                            AND time_id = ? 
                        ) 
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId, date, reservationTimeId));
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
                ),
                ReservationStatus.valueOf(rs.getString("status"))
        );
    }
}
