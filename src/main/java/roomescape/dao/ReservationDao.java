package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.utils.DateTimeConverter;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                DateTimeConverter.timeConverter(rs.getString("time_value"))
        );
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                DateTimeConverter.dateConverter(rs.getString("date")),
                time
        );
    };

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "FROM reservation as r INNER JOIN reservation_time as t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
