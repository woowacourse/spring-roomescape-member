package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Role;
import roomescape.model.Theme;

@Repository
public class ReservationJdbcDao implements ReservationDao {

    private static final String SELECT_RESERVATION = """
            SELECT 
                r.id, 
                r.name, 
                r.date,
                rt.id AS time_id, 
                rt.start_at AS time_start_at,
                t.id AS theme_id, 
                t.name AS theme_name, 
                t.description AS theme_description, 
                t.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.name AS member_name,
                m.email AS member_email,
                m.password AS member_password,
                m.role AS member_role
            FROM reservation AS r
            INNER JOIN reservation_time AS rt ON r.time_id = rt.id
            INNER JOIN theme AS t ON t.id = r.theme_id 
            INNER JOIN member AS m ON m.id = r.member_id 
            """;

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_start_at").toLocalTime()),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("password"),
                            Role.fromValue(resultSet.getString("role"))
                    )
            );

    private final JdbcTemplate jdbcTemplate;

    public ReservationJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_RESERVATION, RESERVATION_ROW_MAPPER);
    }

    public Long saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Reservation> findByDateAndTime(Reservation reservation) {
        String sql = SELECT_RESERVATION + " WHERE r.date = ? AND rt.id = ?";

        return jdbcTemplate.query(
                        sql,
                        RESERVATION_ROW_MAPPER,
                        reservation.getDate(),
                        reservation.getTime().getId()).stream()
                .findFirst();
    }
}
