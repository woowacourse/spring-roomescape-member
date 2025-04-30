package roomescape.dao.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class)
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );

        return Reservation.load(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                time,
                theme
        );
    };

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, reservationMapper);
    }


    @Override
    public Reservation create(final Reservation reservation) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>(Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()));
        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return Reservation.load(key.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void delete(final long id) {
        final String sql = "DELETE reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findByDateAndTime(final Reservation reservation) {
        final String sql = "SELECT " +
                "r.id as reservation_id, " +
                "r.name, " +
                "r.date, " +
                "t.id as time_id, " +
                "t.start_at as time_value " +
                "FROM reservation as r " +
                "inner join reservation_time as t " +
                "on r.time_id = t.id " +
                "WHERE r.date = ? AND r.time_id = ?";
        return jdbcTemplate.query(sql, reservationMapper, reservation.getDate(),
                reservation.getTime().getId()).stream().findFirst();
    }
}
