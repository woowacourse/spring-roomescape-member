package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.domain.ReservationTime.ReservationTimeCondition;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;

@Repository
public class ReservationTimeDao {
    private static final String TABLE_NAME = "reservation_time";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_START_AT = "start_at";
    private static final String COLUMN_AVAILABLE = "available";

    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, start_at FROM reservation_time WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, start_at FROM reservation_time";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_time WHERE id = ?";
    private static final String SELECT_AVAILABLE_SQL = """
            SELECT t.id AS id, t.start_at AS start_at, \s
            CASE \s
            WHEN r.time_id IS NULL THEN true \s
            ELSE false \s
            END AS available \s
            FROM reservation_time t \s
            LEFT JOIN ( \s
            SELECT r.time_id \s
            FROM reservation r \s
            WHERE r.date = ? AND r.theme_id = ? \s
            ) AS r ON r.time_id = t.id
            """;

    private static final RowMapper<ReservationTime> MAPPER = (rs, rowNumber) -> new ReservationTime(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_START_AT)
    );

    private static final RowMapper<ReservationTimeWithAvailable> CONDITION_MAPPER = (rs, rowNumber) -> new ReservationTimeWithAvailable(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_START_AT),
            rs.getBoolean(COLUMN_AVAILABLE)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID);
    }

    public long insertReservationTime(ReservationTimeCommand reservationTimeCommand) {
        return simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_START_AT, reservationTimeCommand.startAt()
        )).longValue();
    }

    public Optional<ReservationTime> getReservationTime(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, MAPPER, id)
                .stream()
                .findFirst();
    }

    public List<ReservationTime> getAllReservationTime() {
        return jdbcTemplate.query(SELECT_ALL_SQL, MAPPER);
    }

    public void deleteReservationTime(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    public List<ReservationTimeWithAvailable> getReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) {
        return jdbcTemplate.query(
                SELECT_AVAILABLE_SQL,
                CONDITION_MAPPER,
                reservationTimeCondition.date(),
                reservationTimeCondition.themeId()
        );
    }
}
