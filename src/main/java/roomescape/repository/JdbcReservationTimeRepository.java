package roomescape.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final String CREATE_SQL =
            "INSERT INTO `reservation_time`(`start_at`) VALUES (?)";
    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM `reservation_time` WHERE `id` = ?";
    private static final String FIND_ALL_SQL =
            "SELECT * FROM `reservation_time` ORDER BY `start_at` ASC";
    private static final String FIND_BOOKED_TIME_IDS_BY_DATE_AND_THEME_SQL = """
            SELECT t.id AS time_id
            FROM `reservation_time` t
            INNER JOIN `reservation` r ON r.time_id = t.id
            WHERE r.date = ? AND r.theme_id = ?""";
    private static final String DELETE_SQL =
            "DELETE FROM `reservation_time` WHERE `id` = ?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime create(ReservationTime reservationTimeWithoutId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, new String[]{"id"});
            preparedStatement.setTime(1, Time.valueOf(reservationTimeWithoutId.getStartAt()));

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return ReservationTime.of(id, reservationTimeWithoutId);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, this::mapToReservationTime, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, this::mapToReservationTime);
    }

    @Override
    public List<Long> findIdsByDateAndTheme(LocalDate date, Long themeId) {
        return jdbcTemplate.query(FIND_BOOKED_TIME_IDS_BY_DATE_AND_THEME_SQL,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date,
                themeId
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private ReservationTime mapToReservationTime(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        LocalTime startAt = resultSet.getTime("start_at").toLocalTime();

        return new ReservationTime(id, startAt);
    }
}
