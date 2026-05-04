package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.InfrastructureException;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final String FIND_ALL_SQL = """
            SELECT id, start_at
            FROM reservation_time
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, start_at
            FROM reservation_time
            WHERE id = ?
            """;

    private static final String INSERT_SQL = """
            INSERT INTO reservation_time (start_at)
            VALUES (?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM reservation_time
            WHERE id = ?
            """;

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = insert(reservationTime, keyHolder);
        validateCreatedRowCount(rowCount);

        Long id = getGeneratedId(keyHolder);
        return reservationTime.withId(id);
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, reservationTimeRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, reservationTimeRowMapper, id)
                .stream()
                .findFirst();
    }

    private int insert(ReservationTime reservationTime, KeyHolder keyHolder) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, reservationTime.getStartAt().toString());
            return preparedStatement;
        }, keyHolder);
    }

    private void validateCreatedRowCount(int rowCount) {
        if (rowCount != 1) {
            throw new InfrastructureException(ErrorCode.RESERVATION_TIME_CREATE_FAILED);
        }
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InfrastructureException(ErrorCode.RESERVATION_TIME_CREATE_FAILED);
        }
        return key.longValue();
    }
}

