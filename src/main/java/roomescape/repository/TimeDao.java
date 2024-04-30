package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation_time (start_at) VALUES (?)",
                    new String[]{"id"});
            ps.setString(1, reservationTime.getStartAt().toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<ReservationTime> findById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, timeRowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        List<ReservationTime> reservationTimes = jdbcTemplate.query("SELECT * FROM reservation_time", timeRowMapper);
        return Collections.unmodifiableList(reservationTimes);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}
