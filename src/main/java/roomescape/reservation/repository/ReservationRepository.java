package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, new String[]{"id"}
            );
            ps.setString(1, reservation.getName());
            ps.setString(2, String.valueOf(reservation.getDate()));
            ps.setString(3, String.valueOf(reservation.getTime().getId()));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                select
                r.id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at
                from reservation r
                inner join reservation_time t
                on r.time_id = t.id
                where r.id = ?
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, createReservationRowMapper(), id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Reservation> findAll() {
        String sql = """
                select
                r.id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at
                from reservation r 
                inner join reservation_time t
                on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, createReservationRowMapper());
    }

    public boolean existReservation(Reservation reservation) {
        String sql = """
                select count(*)
                from reservation r
                join reservation_time t on r.time_id = t.id
                where r.date = ? and t.start_at = ?
                """;
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, reservation.getDate().toString(),
                    reservation.getTime().getStartAt().toString());
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Reservation> createReservationRowMapper() {
        return (rs, rowNum) -> {
            return new Reservation(
                    rs.getLong("id"),
                    new Name(rs.getString("name")),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    )
            );
        };
    }
}
