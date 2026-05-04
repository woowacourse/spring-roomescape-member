package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;

@Repository
public class ReservationDao {
    private static final String FAILED_ID_GENERATE = "ID 생성에 실패하였습니다.";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";

    private static final String ALIAS_TIME_ID = "timeId";
    private static final String ALIAS_START_AT = "startAt";

    private static final String SELECT_ALL_SQL = """
        SELECT\s
            r.id AS id,\s
            r.name AS name,\s
            r.date AS date,\s
            t.id AS timeId,\s
            t.start_at AS startAt\s
        FROM reservation AS r\s
        JOIN reservation_time AS t ON r.time_id = t.id
    """;
    private static final String INSERT_SQL = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation WHERE id = ?";
    private static final String EXIST_BY_TIME_ID_SQL = """
            SELECT EXISTS (\s
                SELECT 1 \s
                    FROM reservation \s
                    WHERE time_id = ?\s
            )
    """;

    private static final RowMapper<Reservation> MAPPER = (rs, rowNumber) -> new Reservation(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_NAME),
            rs.getString(COLUMN_DATE),
            new ReservationTime(
                    rs.getLong(ALIAS_TIME_ID),
                    rs.getString(ALIAS_START_AT)
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> getAllReservation() {
        return jdbcTemplate.query(SELECT_ALL_SQL, MAPPER);
    }

    public long insertReservation(ReservationCommand reservationCommand) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[] { COLUMN_ID });
            statement.setString(1, reservationCommand.name());
            statement.setString(2, reservationCommand.date());
            statement.setLong(3, reservationCommand.timeId());
            return statement;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new RuntimeException(FAILED_ID_GENERATE);
        }
        return key.longValue();
    }

    public void deleteReservation(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    public boolean existsByTimeId(long timeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_BY_TIME_ID_SQL, Boolean.class, timeId));
    }
}
