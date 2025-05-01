package roomescape.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@Repository
public class JdbcReservationDAO implements ReservationDAO {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()),
            new RoomTheme(resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationDAO(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(final Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existSameReservation(final LocalDate date, final long timeId, final long themeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId));
    }

    @Override
    public List<Reservation> findAll() {
        final String query = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    rt.id AS time_id,
                    rt.start_at AS time_value,
                    t.theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t 
                ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(query, RESERVATION_ROW_MAPPER);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        final String query = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    rt.id AS time_id,
                    rt.start_at AS time_value,
                    t.theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t 
                ON r.theme_id = t.id
                WHERE r.id = ?
                """;
        List<Reservation> reservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER, id);
        return reservations.stream()
                .findFirst();
    }

    @Override
    public boolean existsByTimeId(final long timeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(final long themeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    @Override
    public boolean deleteById(final long id) {
        final String query = "DELETE FROM reservation WHERE id = ?";
        final int deleted = jdbcTemplate.update(query, id);
        return deleted > 0;
    }
}
