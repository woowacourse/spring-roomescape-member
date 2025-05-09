package roomescape.reservation.infrastructure;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
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

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> Reservation.createWithId(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            ReservationTime.createWithId(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
            ),
            Theme.createWithId(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", Date.valueOf(reservation.getDate()));
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
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
    public boolean deleteBy(final Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int count = jdbcTemplate.update(sql, id);

        return count != 0;
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
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean hasSameReservation(Reservation reservation) {
        String sql = """
                SELECT EXISTS(            
                    SELECT 1
                    FROM reservation as r
                    INNER JOIN reservation_time as t ON r.time_id = t.id
                    INNER JOIN theme as th ON r.theme_id = th.id
                    WHERE r.date = ? and t.start_at = ? and th.id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class,
                Date.valueOf(reservation.getDate()),
                Time.valueOf(reservation.getReservationTime()),
                reservation.getThemeId());
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }
}
