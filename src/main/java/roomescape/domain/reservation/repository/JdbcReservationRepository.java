package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("reservation")
            .usingColumns("name", "date", "time_id", "theme_id")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findReservationsByDeletedAtIsNull() {
        return jdbcTemplate.query(
            """
                SELECT r.id, r.name, r.date,
                       rt.id AS time_id, rt.start_at,
                       t.id AS theme_id, t.name AS theme_name, t.description, t.image_url
                FROM reservation r
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                WHERE r.deleted_at IS NULL
                """,
            (rs, rowNum) -> mapReservation(rs)
        );
    }

    @Override
    public List<Long> findTimeIdsByDateAndThemeIdAndDeletedAtIsNull(LocalDate date, Long themeId) {
        String sql = """
            SELECT time_id
            FROM reservation r
            JOIN reservation_time rt ON r.time_id = rt.id
            JOIN theme t ON r.theme_id = t.id
            WHERE r.date = :date
              AND r.theme_id = :themeId
              AND r.deleted_at IS NULL
              AND rt.deleted_at IS NULL
              AND t.deleted_at IS NULL
            """;
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
            "date", date,
            "themeId", themeId
        ));

        return jdbcTemplate.query(
            sql,
            parameters,
            (resultSet, rowNum) -> resultSet.getLong("time_id"));
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> args = Map.of(
            "name", reservation.getName(),
            "date", reservation.getDate(),
            "time_id", reservation.getTime().getId(),
            "theme_id", reservation.getTheme().getId()
        );
        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(args).longValue();

        return Reservation.reconstruct(generatedKey, reservation.getName(), reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme());
    }

    private Reservation mapReservation(java.sql.ResultSet rs) throws java.sql.SQLException {
        return Reservation.reconstruct(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            Time.reconstruct(
                rs.getLong("time_id"),
                rs.getTime("start_at").toLocalTime()
            ),
            Theme.reconstruct(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("image_url")
            )
        );
    }

    @Override
    public void deleteReservationById(Long id) {
        String sql = "UPDATE reservation SET deleted_at = CURRENT_TIMESTAMP WHERE id = :id AND deleted_at IS NULL";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public boolean existsReservationByIdAndDeletedAtIsNull(Long id) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM reservation
                WHERE id = :id
                  AND deleted_at IS NULL
            )
            """;

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        Boolean exists = jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean existsReservationByDateAndTimeAndThemeAndDeletedAtIsNull(LocalDate date, Time time, Theme theme) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM reservation
                WHERE date = :date
                  AND time_id = :timeId
                  AND theme_id = :themeId
                  AND deleted_at IS NULL
            )
            """;

        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
            "date", date,
            "timeId", time.getId(),
            "themeId", theme.getId()
        ));

        Boolean exists = jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
        return Boolean.TRUE.equals(exists);
    }
}
