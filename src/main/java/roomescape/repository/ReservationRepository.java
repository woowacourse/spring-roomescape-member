package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {
    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> Reservation.of(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getString("date"),
            ReservationTime.of(resultSet.getLong("time_id"), resultSet.getString("start_at")),
            Theme.of(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("description"), resultSet.getString("thumbnail_url")));

    private static final String SELECT_ALL = """
            SELECT r.id   AS reservation_id,
                   r.name,
                   r.date,
                   rt.id  AS time_id,
                   rt.start_at,
                   t.id   AS theme_id,
                   t.name AS theme_name,
                   t.description,
                   t.thumbnail_url
            FROM reservation r
            INNER JOIN reservation_time rt ON r.time_id  = rt.id
            INNER JOIN theme             t  ON r.theme_id = t.id
            """;
    private static final String SELECT_BY_ID = SELECT_ALL + "WHERE r.id = ?";
    private static final String SELECT_BY_TIME_AND_THEME = SELECT_ALL + "WHERE r.time_id = ? AND r.theme_id = ?";
    private static final String SELECT_BY_NAME = SELECT_ALL + "WHERE r.name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<Reservation> findById(long reservationId) {
        List<Reservation> result = jdbcTemplate.query(SELECT_BY_ID, RESERVATION_ROW_MAPPER, reservationId);
        return result.stream().findFirst();
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_ALL, RESERVATION_ROW_MAPPER);
    }

    public List<Reservation> findByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME, RESERVATION_ROW_MAPPER, name);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate().getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Reservation.of(generatedKey, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(long id, LocalDate date, long timeId) {
        String sql = "UPDATE reservation SET date = ?, time_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, date, timeId, id);
    }

    public List<Reservation> findByTimeAndTheme(Long timeId, Long themeId) {
        return jdbcTemplate.query(SELECT_BY_TIME_AND_THEME, RESERVATION_ROW_MAPPER, timeId, themeId);
    }

    public boolean existsByTimeId(long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    public boolean existsByThemeId(long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }
}
