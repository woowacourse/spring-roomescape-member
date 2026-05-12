package roomescape.repository.reservationTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final String TABLE_NAME = "reservation_time";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_START_AT = "start_at";
    private static final String COLUMN_AVAILABLE = "available";

    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, start_at FROM reservation_time WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, start_at FROM reservation_time";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_time WHERE id = ?";
    private static final String SELECT_AVAILABLE_SQL = """
            SELECT t.id AS id, t.start_at AS start_at,
            CASE
            WHEN r.time_id IS NULL THEN true
            ELSE false
            END AS available
            FROM reservation_time t
            LEFT JOIN (
            SELECT r.time_id
            FROM reservation r
            WHERE r.date = ? AND r.theme_id = ?
            ) AS r ON r.time_id = t.id
            """;

    private static final RowMapper<ReservationTime> MAPPER = (rs, rowNumber) -> new ReservationTime(
            rs.getLong(COLUMN_ID),
            rs.getObject(COLUMN_START_AT, LocalTime.class)
    );

    private static final RowMapper<ReservationTimeWithAvailable> CONDITION_MAPPER = (rs, rowNumber) -> new ReservationTimeWithAvailable(
            rs.getLong(COLUMN_ID),
            rs.getObject(COLUMN_START_AT, LocalTime.class),
            rs.getBoolean(COLUMN_AVAILABLE)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID);
    }

    @Override
    public ReservationTime addReservationTime(ReservationTime reservationTime) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_START_AT, reservationTime.startAt()
        )).longValue();

        return new ReservationTime(id, reservationTime.startAt());
    }

    @Override
    public Optional<ReservationTime> getReservationTime(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> getAllReservationTime() {
        return Collections.unmodifiableList(jdbcTemplate.query(SELECT_ALL_SQL, MAPPER));
    }

    @Override
    public void deleteReservationTime(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    @Override
    public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) {
        return jdbcTemplate.query(
                SELECT_AVAILABLE_SQL,
                CONDITION_MAPPER,
                reservationTimeCondition.date(),
                reservationTimeCondition.themeId()
        );
    }
}
