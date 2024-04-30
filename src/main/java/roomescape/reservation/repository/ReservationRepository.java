package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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

        return keyHolder.getKey().longValue();
    }

    public Reservation findById(Long id) {
        String sql = """
                select
                r.id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at
                from reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                where r.id = ?
                """;

        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    return new Reservation(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getDate("date").toLocalDate(),
                            new ReservationTime(
                                    rs.getLong("time_id"),
                                    rs.getTime("start_at").toLocalTime()
                            )
                    );
                }, id);
    }

    public List<Reservation> findAll() {
        String sql = """
                select
                r.id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at
                from reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    return new Reservation(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getDate("date").toLocalDate(),
                            new ReservationTime(
                                    rs.getLong("id"),
                                    rs.getTime("start_at").toLocalTime()
                            )
                    );
                });
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
