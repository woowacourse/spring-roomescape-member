package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class TimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getObject("start_at", LocalTime.class)
    );

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public List<ReservationTime> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql =
                "SELECT id, start_at FROM reservation_time " +
                        "EXCEPT " +
                        "SELECT t.id, t.start_at FROM reservation r " +
                        "JOIN reservation_time t ON r.time_id = t.id " +
                        "WHERE r.date = ? AND r.theme_id = ?";
        return jdbcTemplate.query(sql, timeRowMapper, date, themeId);
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public ReservationTime save(LocalTime startAt) {
        String sql = "insert into reservation_time(start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setObject(1, startAt);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, startAt);
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, timeRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, startAt) > 0;
    }
}
