package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, __) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getDate("date").toLocalDate(),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password"),
                    resultSet.getString("member_name"),
                    Role.from(resultSet.getString("role"))
            ),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_start_at").toLocalTime()
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
                + "r.id, r.date, "
                + "member_id, member.email AS member_email, "
                + "member.password AS member_password, member.name AS member_name,"
                + "member.role AS member_role, "
                + "time_id, time.start_at AS time_start_at, "
                + "theme_id, theme.name AS theme_name, "
                + "theme.description AS theme_description, theme.thumbnail as theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS time "
                + "ON r.time_id = time.id "
                + "INNER JOIN THEME "
                + "ON r.theme_id = theme.id "
                + "INNER JOIN MEMBER "
                + "ON r.member_id = member.id";
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    public Optional<Reservation> findById(long id) {
        String query = "SELECT "
                + "r.id, r.date, "
                + "member_id, member.email AS member_email, "
                + "member.password AS member_password, member.name AS member_name, "
                + "time_id, time.start_at AS time_start_at, "
                + "theme_id, theme.name AS theme_name, "
                + "theme.description AS theme_description, theme.thumbnail as theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS time "
                + "ON r.time_id = time.id "
                + "INNER JOIN THEME "
                + "ON r.theme_id = theme.id "
                + "INNER JOIN MEMBER "
                + "ON r.member_id = member.id "
                + "WHERE r.id = ?";
        try {
            Reservation reservation = jdbcTemplate.queryForObject(query, reservationRowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.date())
                .addValue("member_id", reservation.member().id())
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.theme().id());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation.date(), reservation.member(), reservation.time(), reservation.theme());
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
