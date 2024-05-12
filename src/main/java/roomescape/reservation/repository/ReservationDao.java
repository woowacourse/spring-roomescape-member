package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, __) -> new Reservation(
            resultSet.getLong("id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("is_admin")
            ),
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
                + "r.id, "
                + "m.id AS member_id, m.name AS member_name, m.email, m.password, m.is_admin, "
                + "r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id "
                + "INNER JOIN MEMBER AS m "
                + "ON r.member_id = m.id ";
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.theme().id())
                .addValue("member_id", reservation.member().id());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    private Reservation findById(long id) {
        String query = "SELECT "
                + "r.id, "
                + "m.id AS member_id, m.name AS member_name, m.email, m.password, m.is_admin, "
                + "r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id "
                + "INNER JOIN MEMBER AS m "
                + "ON r.member_id = m.id "
                + "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(query, reservationRowMapper, id);
    }

    public boolean existsTime(long id) {
        String query = "SELECT COUNT(*) FROM RESERVATION WHERE TIME_ID = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);
        return count != null && count > 0;
    }

    public int deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM RESERVATION WHERE ID = ?", id);
    }

    public boolean isDuplicate(LocalDate date, long timeId, long themeId) {
        String query = "SELECT COUNT(*) FROM RESERVATION WHERE time_id = ? AND date = ? AND theme_id=?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, timeId, date, themeId);
        return count != null && count > 0;
    }
}
