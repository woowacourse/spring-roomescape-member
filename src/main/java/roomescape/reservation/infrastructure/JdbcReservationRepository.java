package roomescape.reservation.infrastructure;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
@Primary
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", Date.valueOf(reservation.getDate()));
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Reservation> findBy(LocalDate date, Long themeId) {
        String sql = """               
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON th.id = r.theme_id
                WHERE th.id = ? AND r.date = ?
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER, themeId, Date.valueOf(date));
    }

    @Override
    public Reservation findById(final Long id) {
        String sql = """               
                SELECT
                    r.id as reservation_id,
                    r.name as name,
                    r.date as date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as decription,
                    th.thumbnail as thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON th.id = r.theme_id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    @Override
    public int deleteById(final Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """               
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON th.id = r.theme_id
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existByReservationTimeId(final Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, timeId);
        return count != 0;
    }

    @Override
    public boolean existBy(Long themeId, LocalDate date, LocalTime time) {
        String sql = """               
                SELECT COUNT(*)
                FROM reservation as r
                INNER JOIN reservation_time as t
                INNER JOIN theme as th
                ON r.time_id = t.id
                WHERE r.date = ? and t.start_at = ? and th.id = ?
                """;
        Long count = jdbcTemplate.queryForObject(sql, Long.class, Date.valueOf(date), Time.valueOf(time), themeId);
        return count != 0;
    }

    @Override
    public boolean existBy(final Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, themeId);
        return count != 0;
    }
}
