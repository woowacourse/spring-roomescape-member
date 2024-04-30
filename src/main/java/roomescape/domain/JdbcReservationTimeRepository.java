package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", reservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    "SELECT id, start_at FROM reservation_time WHERE id = ?",
                    reservationTimeRowMapper(), id);
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public ReservationTime create(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt().format(TIME_FORMATTER));
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));
            ReservationTime reservationTime = new ReservationTime(resultSet.getLong("id"), startAt);
            return reservationTime;
        };
    }

    public void removeById(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("해당 시간을 사용하고 있는 예약이 존재합니다.");
        }
    }

    public boolean hasDuplicateTime(ReservationTime reservationTime) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT count(*) 
                        FROM reservation_time 
                        WHERE start_at = ?""",
                Integer.class, reservationTime.getStartAt()) > 0;
    }
}
