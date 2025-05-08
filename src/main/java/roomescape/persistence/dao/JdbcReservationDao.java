package roomescape.persistence.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.presentation.dto.ReservationAvailableTimeResponse;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private static final String RESERVATION_ID = "id";
    private static final String RESERVATION_NAME = "name";
    private static final String RESERVATION_DATE = "date";
    private static final String TIME_ID = "time_id";
    private static final String TIME_START_AT = "time_start_at";
    private static final String THEME_ID = "theme_id";
    private static final String THEME_NAME = "theme_name";
    private static final String THEME_DESCRIPTION = "theme_description";
    private static final String THEME_THUMBNAIL = "theme_thumbnail";
    private static final RowMapper<Reservation> reservationFullRowMapper =
            (rs, rowNum) -> new Reservation(
                    rs.getLong(RESERVATION_ID),
                    rs.getString(RESERVATION_NAME),
                    LocalDate.parse(rs.getString(RESERVATION_DATE)),
                    new PlayTime(rs.getLong(TIME_ID),
                            LocalTime.parse(rs.getString(TIME_START_AT))),
                    new Theme(
                            rs.getLong(THEME_ID),
                            rs.getString(THEME_NAME),
                            rs.getString(THEME_DESCRIPTION),
                            rs.getString(THEME_THUMBNAIL)
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns(RESERVATION_ID);
    }

    @Override
    public Reservation insert(final Reservation reservation) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(RESERVATION_NAME, reservation.getName());
        parameters.put(RESERVATION_DATE, reservation.getDate().toString());
        parameters.put(TIME_ID, reservation.getPlayTime().getId());
        parameters.put(THEME_ID, reservation.getTheme().getId());
        final Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(),
                reservation.getPlayTime(), reservation.getTheme());
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS rt
                        ON r.time_id = rt.id 
                    INNER JOIN theme AS t 
                        ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationFullRowMapper);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = """
                SELECT
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS rt
                        ON r.time_id = rt.id 
                    INNER JOIN theme AS t 
                        ON r.theme_id = t.id
                WHERE r.id = ?
                """;
        try {
            final Reservation reservation = jdbcTemplate.queryForObject(sql, reservationFullRowMapper, id);
            return Optional.of(reservation);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(final Long id) {
        final String sql = """
                DELETE FROM reservation 
                WHERE id = ?
                """;
        final int updatedRowCount = jdbcTemplate.update(sql, id);
        return updatedRowCount >= 1;
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        final String sql = """
                SELECT EXISTS (
                    SELECT 1 
                    FROM reservation 
                    WHERE date = ? AND time_id = ? AND theme_id = ?
                ) AS is_exists
                """;
        final int flag = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return flag == 1;
    }

    @Override
    public List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndThemeId(final LocalDate date,
                                                                                     final Long themeId) {
        final String sql = """
                SELECT 
                    start_at, 
                    t.id AS time_id,                 
                    r.id AS reservation_id
                FROM
                    reservation_time AS t
                    LEFT JOIN ( 
                        SELECT *
                        FROM reservation
                        WHERE date = ? and theme_id = ?
                    ) AS r 
                    ON t.id = r.time_id
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ReservationAvailableTimeResponse(
                        rs.getString(1),
                        rs.getLong(2),
                        rs.getLong(3) != 0
                ),
                date,
                themeId
        );
    }
}
