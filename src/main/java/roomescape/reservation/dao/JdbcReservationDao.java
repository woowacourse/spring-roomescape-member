package roomescape.reservation.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
        return Reservation.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                reservationTime,
                theme
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                """;

        return this.jdbcTemplate.query(sql,
                reservationRowMapper);
    }

    @Override
    public Reservation create(Reservation reservation) {
        String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getReservationTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return reservation.withId(id);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findByTimeId(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.time_id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.id= ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.date = ? AND rt.start_at = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, date, time);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
