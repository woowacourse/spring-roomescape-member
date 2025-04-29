package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class RoomescapeRepositoryImpl implements RoomescapeRepository {

    private final JdbcTemplate template;

    public RoomescapeRepositoryImpl(final JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Reservation findById(final long id) {
        String sql = joinReservationAndTime("WHERE r.id = ?");
        return template.queryForObject(sql, reservationRowMapper(), id);
    }

    @Override
    public List<Reservation> findByDate(final LocalDate date) {
        String sql = joinReservationAndTime("WHERE r.date = ?");
        return template.query(sql, reservationRowMapper(), date.toString());
    }

    @Override
    public List<Reservation> findAll() {
        String sql = joinReservationAndTime("");
        return template.query(sql, reservationRowMapper());
    }

    @Override
    public Reservation save(final Reservation reservation) {
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return reservation.toEntity(id);
    }

    @Override
    public int deleteById(final long id) {
        String sql = "delete from reservation where id = ?";
        return template.update(sql, id);
    }

    @Override
    public boolean existsByDateAndTime(final LocalDate date, final ReservationTime time) {
        String sql = wrapExistsQuery(joinReservationAndTime("WHERE r.date = ? and t.start_at = ?"));
        return template.queryForObject(sql, Boolean.class, date.toString(), time.getStartAt().toString());
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getString("time_value")
            );
            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    reservationTime
            );
        };
    }

    private String wrapExistsQuery(String sql) {
        return "SELECT EXISTS(" + sql + ")";
    }

    private String joinReservationAndTime(String where) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.start_at AS time_value
                FROM reservation as r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                """;
        return sql + where;
    }
}
