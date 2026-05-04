package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;

@Repository
public class ReservationTimeDao {
    private static final String FAILED_ID_GENERATE = "ID 생성에 실패하였습니다.";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_START_AT = "start_at";

    private static final String INSERT_SQL = "INSERT INTO reservation_time (start_at) VALUES (?)";
    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, start_at FROM reservation_time WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, start_at FROM reservation_time";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_time WHERE id = ?";

    private static final RowMapper<ReservationTime> MAPPER = (rs, rowNumber) -> new ReservationTime(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_START_AT)
    );

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insertReservationTime(ReservationTimeCommand reservationTimeCommand) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[] { COLUMN_ID });
            statement.setString(1, reservationTimeCommand.startAt());
            return statement;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        
        if(key == null) {
            throw new RuntimeException(FAILED_ID_GENERATE);
        }

        return key.longValue();
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
}
