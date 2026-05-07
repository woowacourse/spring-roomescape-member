package roomescape.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

@Repository
public class ReservationJdbcTemplateRepository implements ReservationRepository {

    private static final String FIND_BY_DATE_AND_THEME_ID_QUERY = """
            SELECT r.id,
                   r.name AS reservation_name,
                   r.date,
                   rt.id AS time_id,
                   rt.start_at,
                   t.id AS theme_id,
                   t.name AS theme_name,
                   t.description AS theme_description,
                   t.thumbnail_url
            FROM reservation r
            JOIN reservation_time rt ON r.time_id = rt.id
            JOIN theme t ON r.theme_id = t.id
            WHERE (? IS NULL OR r.date = ?)
              AND (? IS NULL OR r.theme_id = ?)
            ORDER BY r.id
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT r.id,
                   r.name AS reservation_name,
                   r.date,
                   rt.id AS time_id,
                   rt.start_at,
                   t.id AS theme_id,
                   t.name AS theme_name,
                   t.description AS theme_description,
                   t.thumbnail_url
            FROM reservation r
            JOIN reservation_time rt ON r.time_id = rt.id
            JOIN theme t ON r.theme_id = t.id
            WHERE r.id = ?
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT r.id,
                   r.name AS reservation_name,
                   r.date,
                   rt.id AS time_id,
                   rt.start_at,
                   t.id AS theme_id,
                   t.name AS theme_name,
                   t.description AS theme_description,
                   t.thumbnail_url
            FROM reservation r
            JOIN reservation_time rt ON r.time_id = rt.id
            JOIN theme t ON r.theme_id = t.id
            ORDER BY r.id
            """;
    private static final String EXISTS_BY_TIME_ID_QUERY = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                );
            """;
    private static final String EXISTS_BY_THEME_ID_QUERY = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                );
            """;
    private static final String EXISTS_BY_DATE_AND_TIME_ID_AND_THEME_ID_QUERY = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ?
                      AND time_id = ?
                      AND theme_id = ?
                );
            """;

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM reservation WHERE id = ?";

    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> Reservation.createWithId(
            rs.getLong("id"),
            rs.getString("reservation_name"),
            rs.getDate("date").toLocalDate(),
            createReservationTimeFromResultSet(rs),
            createThemeFromResultSet(rs)
    );

    private static ReservationTime createReservationTimeFromResultSet(ResultSet rs) throws SQLException {
        return ReservationTime.createWithId(
                rs.getLong("time_id"),
                rs.getTime("start_at").toLocalTime()
        );
    }

    private static Theme createThemeFromResultSet(ResultSet rs) throws SQLException {
        return Theme.createWithId(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("thumbnail_url")
        );
    }

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> params = prepareInsertParameters(reservation);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.appendId(id);
    }

    private Map<String, Object> prepareInsertParameters(Reservation reservation) {
        return Map.of(
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.time().id(),
                "theme_id", reservation.theme().id()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        List<Reservation> reservation = jdbcTemplate.query(FIND_BY_ID_QUERY, ROW_MAPPER, id);
        return reservation.stream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
    }

    @Override
    public boolean existsByReservationTimeId(Long id) {
        Integer result = jdbcTemplate.queryForObject(EXISTS_BY_TIME_ID_QUERY, Integer.class, id);
        return result != null && result == 1;
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        Integer result = jdbcTemplate.queryForObject(EXISTS_BY_THEME_ID_QUERY, Integer.class, themeId);
        return result != null && result == 1;
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        Integer result = jdbcTemplate.queryForObject(
                EXISTS_BY_DATE_AND_TIME_ID_AND_THEME_ID_QUERY,
                Integer.class,
                date, timeId, themeId
        );
        return result != null && result == 1;
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.query(FIND_BY_DATE_AND_THEME_ID_QUERY, ROW_MAPPER, date, date, themeId, themeId);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }
}
