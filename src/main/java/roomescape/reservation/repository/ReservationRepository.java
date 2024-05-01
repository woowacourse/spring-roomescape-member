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
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        String sql = "insert into reservation (name, date, theme_id, time_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, new String[]{"id"}
            );
            ps.setString(1, reservation.getName());
            ps.setString(2, String.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTheme().getId());
            ps.setLong(4, reservation.getTime().getId());
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
                t.id as theme_id,
                t.name,
                t.description,
                t.thumbnail,
                rt.id as time_id,
                rt.start_at
                from reservation r
                join reservation_time rt
                on r.time_id = rt.id
                join theme t
                on r.theme_id = t.id
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
                t.id as theme_id,
                t.name,
                t.description,
                t.thumbnail,
                rt.id as time_id,
                rt.start_at
                from reservation r
                join reservation_time rt
                on r.time_id = rt.id
                join theme t
                on r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, createReservationRowMapper());
    }

    public boolean existReservation(Reservation reservation) {
        String sql = """
                select exists (select 1
                from reservation r
                join reservation_time t on r.time_id = t.id where r.date = ? and t.start_at = ?)
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, reservation.getDate().toString(),
                reservation.getTime().getStartAt().toString());
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
                    new Theme(
                            rs.getLong("theme_id"),
                            new Name(rs.getString("name")),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    ),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    )
            );
        };
    }
}
