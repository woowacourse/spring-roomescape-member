package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime())
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.start_at AS time_value
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Long save(String name, LocalDate date, Long timeId) {
        return jdbcInsert.executeAndReturnKey(Map.of(
                "name", name,
                "date", date,
                "time_id", timeId
        )).longValue();
    }

    public boolean existsByTimeId(Long timeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE time_id = ?", Integer.class, timeId);
        return count != null && count > 0;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
