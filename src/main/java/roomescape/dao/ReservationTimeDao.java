package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTime save(ReservationTime time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO reservation_time(start_at) VALUES(?)",
                            new String[]{"id"});
                    ps.setTime(1, Time.valueOf(time.getStartAt()));
                    return ps;
                }
                , keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new ReservationTime(id, time.getStartAt());
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                (rs, rowNum) -> {
                    return new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                }
        );
    }

    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(
                "DELETE FROM reservation_time WHERE id = ?",
                id
        );
        return deletedRow == 1;
    }

    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    "SELECT id, start_at FROM reservation_time WHERE id = ?",
                    (rs, rowNum) -> {
                        return new ReservationTime(
                                rs.getLong("id"),
                                rs.getTime("start_at").toLocalTime()
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservationTime);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
