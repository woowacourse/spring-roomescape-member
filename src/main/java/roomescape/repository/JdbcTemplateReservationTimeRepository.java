package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime addTime(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO reservation_time(start_at) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setTime(1, java.sql.Time.valueOf(reservationTime.startAt()));
            return preparedStatement;
        }, keyHolder);

        return new ReservationTime(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                reservationTime.startAt());
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time",
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime()
                ));
    }

    @Override
    public void deleteTime(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ? ", id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    "SELECT id, start_at FROM reservation_time WHERE id = ?",
                    (rs, rowNum) -> new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    ), id);
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        return jdbcTemplate.query(
                "SELECT t.id AS time_id, t.start_at " +
                        "FROM reservation_time t " +
                        "WHERE t.id NOT IN (" +
                        "  SELECT r.time_id FROM reservation r WHERE r.theme_id = ? AND r.date = ?" +
                        ") " +
                        "ORDER BY t.start_at",

                (rs, rowNum) -> {
                    long timeId = rs.getLong("time_id");
                    LocalTime time = rs.getTime("start_at").toLocalTime();

                    ReservationTime reservationTime = new ReservationTime(timeId, time);

                    return reservationTime;
                },
                themeId,
                date
        );
    }
}
