package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InfrastructureException;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final String RESERVATION_TIME_CREATE_FAILED_MESSAGE = "예약 시간 생성에 실패했습니다.";

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
        String sql = """
                SELECT id, start_at
                FROM reservation_time
                """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation_time
                WHERE start_at = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startAt.toString());
        return count != null && count > 0;
    }

    private int insert(ReservationTime reservationTime, KeyHolder keyHolder) {
        String sql = """
                INSERT INTO reservation_time (start_at)
                VALUES (?)
                """;

        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, reservationTime.getStartAt().toString());
            return preparedStatement;
        }, keyHolder);
    }

    private void validateCreatedRowCount(int rowCount) {
        if (rowCount != 1) {
            throw new InfrastructureException(RESERVATION_TIME_CREATE_FAILED_MESSAGE);
        }
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InfrastructureException(RESERVATION_TIME_CREATE_FAILED_MESSAGE);
        }
        return key.longValue();
    }
}
