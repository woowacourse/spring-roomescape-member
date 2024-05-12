package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, __) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String query = "SELECT "
                + "r.id, r.name, r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id";
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    private Reservation findById(long id) {
        String query = "SELECT "
                + "r.id, r.name, r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id "
                + "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(query, reservationRowMapper, id);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.theme().id());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE ID = ?", id);
    }

    public boolean existsById(long id) {
        String query = "SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, id));
    }

    public boolean existsByTimeId(long timeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE TIME_ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    public boolean existsByThemeId(long themeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE THEME_ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    public boolean existsByAttributes(LocalDate date, long timeId, long themeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE DATE = ? AND TIME_ID = ? AND THEME_ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId));
    }
}
