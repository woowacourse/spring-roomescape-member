package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;
import roomescape.exception.InfrastructureException;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final String FIND_ALL_SQL = """
            SELECT
                r.id AS reservation_id,
                r.name,
                r.date,
                t.id AS time_id,
                t.start_at
            FROM reservation r
            INNER JOIN reservation_time t
                ON r.time_id = t.id
            """;

    private static final String INSERT_SQL = """
            INSERT INTO reservation (name, date, time_id)
            VALUES (?, ?, ?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM reservation
            WHERE id = ?
            """;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("date")),
                reservationTime
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, reservationRowMapper);
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = insert(reservation, keyHolder);
        validateCreatedRowCount(rowCount);

        Long id = getGeneratedId(keyHolder);
        return reservation.withId(id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private int insert(Reservation reservation, KeyHolder keyHolder) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_SQL,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate().toString());
            preparedStatement.setLong(3, reservation.getTime().getId());
            return preparedStatement;
        }, keyHolder);
    }

    private void validateCreatedRowCount(int rowCount) {
        if (rowCount != 1) {
            throw new InfrastructureException(ErrorCode.RESERVATION_CREATE_FAILED);
        }
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InfrastructureException(ErrorCode.RESERVATION_CREATE_FAILED);
        }
        return key.longValue();
    }
}
