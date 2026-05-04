package roomescape.repository.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final JdbcTemplate template;

    public JdbcReservationRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Long createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName().value());
            ps.setString(2, dateFormatter.format(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        return key;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?;";

        template.update(sql, id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT id, name, date, time_id FROM reservation;";

        List<Reservation> reservations = template.query(sql, reservationRowMapper());

        return reservations;
    }

    @Override
    public Reservation findById(Long id) {
        String sql = "SELECT id, name, date, time_id FROM reservation WHERE id = ?;";

        Reservation reservation = template.queryForObject(sql, reservationRowMapper(), id);

        return reservation;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return ((rs, rowNum) -> {
            Reservation reservation = new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    new ReservationTime(rs.getLong("time_id"), (LocalTime) null));
            return reservation;
        });
    }
}
