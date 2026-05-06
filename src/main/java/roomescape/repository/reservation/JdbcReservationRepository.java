package roomescape.repository.reservation;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) ->
        new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getString("res_date"),
            new ReservationTime(
                rs.getLong("time_id"),
                rs.getString("time_value")),
            new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_image_url"))
        );

    private final JdbcTemplate template;

    public JdbcReservationRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, res_date, time_id, theme_id) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName().value());
            ps.setString(2, DATE_TIME_FORMATTER.format(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        return reservation.withId(key);
    }

    @Override
    public void deleteById(Long id) {
        int update = template.update("DELETE FROM reservation WHERE id = ?;", id);

        if (update == 0) {
            throw new NoSuchElementException("존재하지 않는 reservation 의 id 입니다. id = " + id);
        }
    }

    @Override
    public List<Reservation> findAll() {
        return template.query(
            "SELECT "
                + "r.id as reservation_id, "
                + "r.name, "
                + "r.res_date, "
                + "t.id as time_id, "
                + "t.start_at as time_value, "
                + "th.id as theme_id, "
                + "th.name as theme_name, "
                + "th.description as theme_description, "
                + "th.image_url as theme_image_url "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as t "
                + "ON r.time_id = t.id "
                + "INNER JOIN theme as th "
                + "ON r.theme_id = th.id",
            RESERVATION_ROW_MAPPER
        );
    }

    @Override
    public Reservation findById(Long id) {
        return template.queryForObject(
            "SELECT "
                + "r.id as reservation_id, "
                + "r.name, "
                + "r.res_date, "
                + "t.id as time_id, "
                + "t.start_at as time_value, "
                + "th.id as theme_id, "
                + "th.name as theme_name, "
                + "th.description as theme_description, "
                + "th.image_url as theme_image_url "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as t "
                + "ON r.time_id = t.id "
                + "INNER JOIN theme as th "
                + "ON r.theme_id = th.id "
                + "WHERE r.id = ?",
            RESERVATION_ROW_MAPPER,
            id);
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        int count = template.queryForObject(
            """
                SELECT 
                    COUNT(1) 
                FROM reservation
                WHERE time_id = ?
                """,
            Integer.class,
            timeId
        );

        return count != 0;
    }
}
