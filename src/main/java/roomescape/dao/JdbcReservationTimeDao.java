package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Primary
@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime create(ReservationTime reservationTimeWithoutId) {
        String sql = "INSERT INTO `reservation_time`(`start_at`) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setTime(1, Time.valueOf(reservationTimeWithoutId.getStartAt()));

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return ReservationTime.of(id, reservationTimeWithoutId);
    }

    @Override
    public ReservationTime read(Long id) {
        String sql = "SELECT * FROM `reservation_time` WHERE `id` = (?)";

        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
                LocalTime startAt = resultSet.getTime("start_at").toLocalTime();
                return new ReservationTime(id, startAt);
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }
    }

    @Override
    public List<ReservationTime> readAll() {
        String sql = "SELECT * FROM `reservation_time` "
                + "ORDER BY start_at ASC";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            LocalTime startAt = resultSet.getTime("start_at").toLocalTime();
            return new ReservationTime(id, startAt);
        });
    }

    @Override
    public List<Long> reservedTimeIdByDateAndTheme(LocalDate date, Long themeId) {
        String sql = "SELECT t.id as time_id "
                + "FROM `reservation_time` t "
                + "INNER JOIN `reservation` r ON r.time_id = t.id "
                + "WHERE r.date = (?) AND r.theme_id = (?) ";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) ->
                        resultSet.getLong("time_id"),
                date,
                themeId
        );
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM `reservation_time` WHERE `id` = (?)";
        jdbcTemplate.update(sql, id);
    }
}
