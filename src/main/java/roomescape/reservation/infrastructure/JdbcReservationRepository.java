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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate, final DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
    public List<Reservation> findBy(final LocalDate date, final Long themeId) {
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
                WHERE th.id = :themeId AND r.date = :date
                """;

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", Date.valueOf(date));

        return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(final Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        int count = namedParameterJdbcTemplate.update(sql, param);
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

        return namedParameterJdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existByReservationTimeId(final Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = :timeId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("timeId", timeId);

        int count = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class).intValue();
        return count != 0;
    }

    @Override
    public boolean existBy(final Long themeId, final LocalDate date, final LocalTime time) {
        String sql = """               
                SELECT COUNT(*)
                FROM reservation as r
                INNER JOIN reservation_time as t ON r.time_id = t.id
                INNER JOIN theme as th ON r.theme_id = th.id
                WHERE r.date = :date and t.start_at = :startAt and th.id = :themeId
                """;

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", Date.valueOf(date))
                .addValue("startAt", time)
                .addValue("themeId", themeId);

        int count = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class).intValue();
        return count != 0;
    }

    @Override
    public boolean existBy(final Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = :themeId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("themeId", themeId);

        int count = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class).intValue();
        return count != 0;
    }
}
