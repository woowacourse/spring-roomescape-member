package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query("SELECT \n"
                        + "    r.id as reservation_id, \n"
                        + "    r.name, \n"
                        + "    r.date, \n"
                        + "    t.id as time_id, \n"
                        + "    t.start_at as time_value \n"
                        + "FROM reservation as r \n"
                        + "inner join reservation_time as t \n"
                        + "on r.time_id = t.id",
                (rs, rowNum) ->
                        new Reservation(
                                rs.getLong("reservation_id"),
                                rs.getString("name"),
                                rs.getDate("date").toLocalDate(),
                                new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime())));
    }

    @Override
    public Long create(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().id());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(Long reservationId) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", reservationId);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    "SELECT \n"
                            + "    r.id as reservation_id, \n"
                            + "    r.name, \n"
                            + "    r.date, \n"
                            + "    t.id as time_id, \n"
                            + "    t.start_at as time_value \n"
                            + "FROM reservation as r \n"
                            + "inner join reservation_time as t \n"
                            + "on r.time_id = t.id \n"
                            + "WHERE r.id = ?",
                    (rs, rowNum) ->
                            new Reservation(
                                    rs.getLong("reservation_id"),
                                    rs.getString("name"),
                                    rs.getDate("date").toLocalDate(),
                                    new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime())),
                    reservationId);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existByTimeId(final Long reservationTimeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reservationTimeId) > 0;
    }
}
