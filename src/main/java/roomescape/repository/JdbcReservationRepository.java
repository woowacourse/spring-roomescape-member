package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            LocalTime.parse(resultSet.getString("time_value"))
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Reservation findById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    @Override
    public List<Long> findThemeReservationCountsForDate(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(*) DESC
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> resultSet.getLong("theme_id"), startDate, endDate);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                     r.id AS reservation_id,
                     r.name,
                     r.date,
                     t.id AS time_id,
                     t.start_at AS time_value,
                     th.id AS theme_id,
                     th.name AS theme_name,
                     th.description AS theme_description,
                     th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date = ? AND th.id = ?
                 """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean existId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existTimeId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existThemeId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}
