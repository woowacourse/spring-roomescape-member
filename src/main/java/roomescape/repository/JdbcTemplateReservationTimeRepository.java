package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
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
}
