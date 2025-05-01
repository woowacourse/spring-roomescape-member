package roomescape.reservation.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT \n"
                + "    r.id as reservation_id, \n"
                + "    r.name, \n"
                + "    r.date, \n"
                + "    rt.id as time_id, \n"
                + "    rt.start_at as time_value, \n"
                + "    t.id as theme_id,\n"
                + "    t.name as theme_name,\n"
                + "    t.description as theme_des,\n"
                + "    t.thumbnail as theme_thumb\n"
                + "FROM reservation as r \n"
                + "inner join reservation_time as rt \n"
                + "on r.time_id = rt.id\n"
                + "inner join theme as t\n"
                + "on t.id = r.theme_id";

        return this.jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    ReservationTime reservationTime = new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getObject("time_value", LocalTime.class)
                    );

                    Theme theme = new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_des"),
                            resultSet.getString("theme_thumb")
                    );

                    return Reservation.of(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("name"),
                            resultSet.getObject("date", LocalDate.class),
                            reservationTime,
                            theme
                    );
                });
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
                SELECT r.id as reservation_id,
                       r.name as reservation_name,
                       r.date as reservation_date,
                       rt.start_at as time_start_at,
                       rt.id as time_id,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description as theme_des,
                       t.thumbnail as theme_thumb
                    FROM reservation as r 
                    INNER JOIN reservation_time as rt ON rt.id = r.time_id 
                    INNER JOIN theme as t ON r.theme_id = t.id
                    WHERE r.time_id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        return Reservation.of(
                                rs.getLong("reservation_id"),
                                rs.getString("reservation_name"),
                                rs.getDate("reservation_date").toLocalDate(),
                                new ReservationTime(rs.getLong("time_id"), rs.getTime("time_start_at").toLocalTime()),
                                theme
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT r.id as reservation_id,
                       r.name as reservation_name,
                       r.date as reservation_date,
                       rt.start_at as time_start_at,
                       rt.id as time_id,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description as theme_des,
                       t.thumbnail as theme_thumb
                    FROM reservation as r 
                    INNER JOIN reservation_time as rt ON rt.id = r.time_id
                    INNER JOIN theme as t ON t.id = r.theme_id
                    WHERE r.id= ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        return Reservation.of(
                                rs.getLong("reservation_id"),
                                rs.getString("reservation_name"),
                                rs.getDate("reservation_date").toLocalDate(),
                                new ReservationTime(rs.getLong("time_id"),
                                        rs.getTime("time_start_at").toLocalTime()),
                                theme
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        String sql = """
                SELECT * FROM reservation as r 
                    INNER JOIN reservation_time as rt ON rt.id = r.id 
                         INNER JOIN theme as t ON r.theme_id = t.id
                         WHERE r.date = ? and rt.start_at = ?""";
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        return Reservation.of(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getDate("date").toLocalDate(),
                                new ReservationTime(rs.getLong("id"), rs.getTime("start_at").toLocalTime()),
                                theme
                        );
                    },
                    date,
                    time
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
