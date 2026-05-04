package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Reservation> reservationMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getObject("date", LocalDate.class),
            new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getObject("start_at", LocalTime.class)
            )
    );

    public List<Reservation> findAll() {
        String selectSql = "SELECT r.id, r.name, r.date, t.id as time_id, t.start_at " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id";
        return jdbcTemplate.query(selectSql, reservationMapper);
    }

    public void removeById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Reservation register(String name, LocalDate date, Long timeId) {
        String sql = "INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setObject(3, timeId);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        String selectSql = "SELECT r.id, r.name, r.date, t.id as time_id, t.start_at " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id " +
                "WHERE r.id = ?";

        return jdbcTemplate.queryForObject(selectSql, reservationMapper, id);
    }
}
