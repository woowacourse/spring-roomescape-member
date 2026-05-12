package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time;";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public Optional<ReservationTime> findBy(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?;";
        List<ReservationTime> result = jdbcTemplate.query(sql, timeRowMapper, id);
        return result.stream().findAny();
    }

    public Long insert(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time(start_at) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            pstmt.setObject(1, reservationTime.getStartAt());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "select count(*) from reservation_time where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "SELECT count(*) FROM reservation_time WHERE start_at = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startAt);
        return count != null && count > 0;
    }

    private final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> {
        return new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class));
    };
}
