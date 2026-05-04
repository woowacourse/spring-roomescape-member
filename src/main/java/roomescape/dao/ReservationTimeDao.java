package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.utils.DateTimeConverter;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            DateTimeConverter.timeConverter(rs.getString("start_at"))
    );

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", timeRowMapper);
    }

    public Long save(ReservationTime time) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, time.getStartAt().toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM reservation_time WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
