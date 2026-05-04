package roomescape.domain.time.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.entity.Time;

@Repository
public class JdbcTimeRepository implements TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Time save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, time.getStartAt().toString());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        return jdbcTemplate.queryForObject(
            "SELECT id, start_at FROM reservation_time WHERE id = ?",
            (rs, rowNum) -> Time.reconstruct(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
            ),
            key
        );
    }

    @Override
    public List<Time> findAllTimes() {
        return jdbcTemplate.query(
            "SELECT id, start_at FROM reservation_time",
            (rs, rowNum) -> Time.reconstruct(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
            )
        );
    }

    @Override
    public Time findTimeById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT id, start_at FROM reservation_time WHERE id = ?",
            (rs, rowNum) -> Time.reconstruct(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
            ),
            id
        );
    }

    @Override
    public void deleteTimeById(Long id) {
        jdbcTemplate.update(
            "DELETE FROM reservation_time WHERE id = ?",
            id
        );
    }
}
