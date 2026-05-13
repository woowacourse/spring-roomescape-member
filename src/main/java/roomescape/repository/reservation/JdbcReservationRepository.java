package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.QueryWithParams;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final String TABLE_NAME = "reservation";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME_ID = "time_id";
    private static final String COLUMN_THEME_ID = "theme_id";

    private static final String ALIAS_TIME_ID = "timeId";
    private static final String ALIAS_START_AT = "startAt";

    private static final String ALIAS_THEME_ID = "themeId";
    private static final String ALIAS_THEME_NAME = "themeName";
    private static final String ALIAS_THEME_DESCRIPTION = "themeDescription";
    private static final String ALIAS_THEME_IMAGE_URL = "themeImageUrl";

    private static final String SELECT_BY_ID_SQL = "SELECT id, name, date, time_id, theme_id FROM reservation WHERE id = ?";

    private static final String SELECT_ALL_SQL = """
        SELECT\s
            r.id AS id,\s
            r.name AS name,\s
            r.date AS date,\s
            rt.id AS timeId,\s
            rt.start_at AS startAt,\s
            t.id AS themeId,\s
            t.name AS themeName,\s
            t.description AS themeDescription,\s
            t.image_url AS themeImageUrl\s
        FROM reservation AS r\s
        JOIN reservation_time AS rt ON r.time_id = rt.id
        JOIN theme AS t ON r.theme_id = t.id
    """;

    private static final String CONDITION_NAME_SQL = "WHERE r.name = ?";

    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation WHERE id = ?";

    private static final String UPDATE_ALL_SPECIFIC_ID_SQL = "UPDATE reservation SET name = ?, date = ?, time_id = ?, theme_id = ? WHERE id = ?";

    private static final String EXIST_BY_TIME_ID_SQL = """
            SELECT EXISTS (\s
                SELECT 1 \s
                    FROM reservation \s
                    WHERE time_id = ?\s
            )
    """;
    private static final String EXIST_BY_THEME_ID_SQL = """
            SELECT EXISTS (\s
                SELECT 1 \s
                    FROM reservation \s
                    WHERE theme_id = ?\s
            )
    """;

    private static final String EXIST_BY_TIME_ID_AND_THEME_ID_AND_DATE = """
            SELECT EXISTS (\s
                SELECT 1 \s
                    FROM reservation \s
                    WHERE time_id = ?\s
                    AND theme_id = ?\s
                    AND date = ?\s
            )
            """;
    private static final RowMapper<Reservation> RESERVATION_MAPPER = (rs, rowNumber) -> new Reservation(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_NAME),
            rs.getDate(COLUMN_DATE).toLocalDate(),
            rs.getLong(COLUMN_TIME_ID),
            rs.getLong(COLUMN_THEME_ID)
    );

    private static final RowMapper<ReservationInfo> MAPPER = (rs, rowNumber) -> new ReservationInfo(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_NAME),
            rs.getDate(COLUMN_DATE).toLocalDate(),
            new ReservationTime(
                    rs.getLong(ALIAS_TIME_ID),
                    rs.getTime(ALIAS_START_AT).toLocalTime()
            ),
            new Theme(
                    rs.getLong(ALIAS_THEME_ID),
                    rs.getString(ALIAS_THEME_NAME),
                    rs.getString(ALIAS_THEME_DESCRIPTION),
                    rs.getString(ALIAS_THEME_IMAGE_URL)
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID)
                .usingColumns(COLUMN_NAME, COLUMN_DATE, COLUMN_TIME_ID, COLUMN_THEME_ID);
    }

    @Override
    public List<ReservationInfo> getAllReservation(String name) {
        QueryWithParams queryWithParams = getReservationsQuery(name);
        return Collections.unmodifiableList(jdbcTemplate.query(queryWithParams.query(), MAPPER, queryWithParams.params().toArray()));
    }

    @Override
    public Optional<Reservation> getReservation(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, RESERVATION_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public ReservationInfo addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_NAME, reservationCommand.name(),
                COLUMN_DATE, reservationCommand.date(),
                COLUMN_TIME_ID, reservationCommand.timeId(),
                COLUMN_THEME_ID, reservationCommand.themeId()
        )).longValue();

        return new ReservationInfo(id, reservationCommand.name(), reservationCommand.date(), reservationTime,
                theme);
    }

    @Override
    public void deleteReservation(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    @Override
    public int updateAll(long id, ReservationCommand command) {
        return jdbcTemplate.update(UPDATE_ALL_SPECIFIC_ID_SQL, command.name(), command.date(), command.timeId(), command.themeId(), id);
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_BY_TIME_ID_SQL, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_BY_THEME_ID_SQL, Boolean.class, themeId));
    }

    @Override
    public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_BY_TIME_ID_AND_THEME_ID_AND_DATE,
                Boolean.class, timeId, themeId, date));
    }

    private QueryWithParams getReservationsQuery(String name) {
        StringBuilder query = new StringBuilder(SELECT_ALL_SQL);
        List<String> params = new ArrayList<>();

        if(name != null && !name.isBlank()) {
            query.append(CONDITION_NAME_SQL);
            params.add(name);
        }

        return new QueryWithParams(query.toString(), params);
    }
}
