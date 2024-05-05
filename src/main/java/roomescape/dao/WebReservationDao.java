package roomescape.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

@Repository
public class WebReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public WebReservationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Reservation> readAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.`date`,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM
                    reservation r
                INNER JOIN
                    reservation_time t ON r.time_id = t.id
                INNER JOIN
                    theme th ON r.theme_id = th.id;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper());
    }

    @Override
    public List<Long> readTimeIdsByDateAndThemeId(ReservationDate reservationDate, Long themeId) {
        String sql = """
                SELECT
                time_id
                FROM
                reservation
                WHERE date = ? AND theme_id = ?
                """;
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                reservationDate.toStringDate(),
                themeId);
    }

    @Override
    public List<Long> readPopularThemeIds(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(*) DESC, theme_id
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> resultSet.getLong("theme_id"), startDate, endDate
        );
    }

    @Override
    public Reservation create(Reservation reservation) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("reservation").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", reservation.getName().getValue());
        params.put("date", reservation.getDate().getValue());
        params.put("time_id", reservation.getReservationTime().getId());
        params.put("theme_id", reservation.getTheme().getId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean exist(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                reservationDate.toStringDate(),
                reservationTime.getId(),
                theme.getId()
        );
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE
                FROM reservation
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                new ReservationName(resultSet.getString("name")),
                ReservationDate.from(resultSet.getString("date")),
                new ReservationTime(resultSet.getLong("time_id"),
                        ReservationStartAt.from(resultSet.getString("time_value"))),
                new Theme(resultSet.getLong("theme_id"),
                        ThemeName.from(resultSet.getString("theme_name")),
                        ThemeDescription.from(resultSet.getString("theme_description")),
                        ThemeThumbnail.from(resultSet.getString("theme_thumbnail")))
        );
    }
}
