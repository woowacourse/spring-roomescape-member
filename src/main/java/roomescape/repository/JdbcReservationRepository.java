package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        LocalDate date = rs.getObject("date", LocalDate.class);

        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("time_start_at", LocalTime.class)
        );

        Theme theme = new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_thumbnail")
        );

        return new Reservation(id, name, date, time, theme);
    };

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
                        r.id,
                        r.name,
                        r.date,
                        t.id AS time_id,
                        t.start_at AS time_start_at,
                        th.id AS theme_id,
                        th.name AS theme_name,
                        th.description AS theme_description,
                        th.thumbnail AS theme_thumbnail
                    FROM reservation AS r
                    JOIN reservation_time AS t
                    ON r.time_id = t.id
                    JOIN theme AS th
                    ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Reservation save(Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByTimeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByThemeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                    SELECT
                        r.id,
                        r.name,
                        r.date,
                        t.id AS time_id,
                        t.start_at AS time_start_at,
                        th.id AS theme_id,
                        th.name AS theme_name,
                        th.description AS theme_description,
                        th.thumbnail AS theme_thumbnail
                    FROM reservation AS r
                    JOIN reservation_time AS t
                    ON r.time_id = t.id
                    JOIN theme AS th
                    ON r.theme_id = th.id
                    WHERE r.date = ? AND r.theme_id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }
}
