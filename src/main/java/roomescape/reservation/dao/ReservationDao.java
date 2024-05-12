package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Reservation> rowMapper = ((resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    Role.valueOf(resultSet.getString("role"))
            ),
            new ReservationDate(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getString("time_value")
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            )
    ));

    public List<Reservation> findAll() {
        final String sql = """ 
                SELECT r.id AS reservation_id, r.date, 
                       rt.id AS time_id, rt.start_at AS time_value, 
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail, 
                       m.id AS member_id, m.name, m.email, m.role 
                FROM reservation r 
                INNER JOIN reservation_time rt ON r.time_id = rt.id 
                INNER JOIN theme t ON r.theme_id = t.id 
                INNER JOIN member m ON r.member_id = m.id 
                ORDER BY  r.date ASC, rt.start_at ASC;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public long save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", reservation.getReservationDate().getDate().toString())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Reservation> search(final long themeId, final long memberId, final LocalDate startDate, final LocalDate endDate) {
        final String sql = """
                SELECT r.id AS reservation_id, r.date, 
                       rt.id AS time_id, rt.start_at AS time_value, 
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail, 
                       m.id AS member_id, m.name, m.email, m.role 
                FROM reservation AS r 
                INNER JOIN reservation_time rt ON r.time_id = rt.id 
                INNER JOIN theme t ON r.theme_id = t.id 
                INNER JOIN member m ON r.member_id = m.id 
                WHERE r.member_id = ? 
                  AND r.date BETWEEN ? AND ? 
                  AND r.theme_id = ? ORDER BY r.date;
                """;
        return jdbcTemplate.query(sql, rowMapper, memberId, startDate.toString(), endDate.toString(), themeId);
    }

    public int deleteById(final long id) {
        final String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean checkExistByReservation(final LocalDate date, final long timeId, final long themeId) {
        final String sql = """
                SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END 
                FROM reservation AS r 
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id 
                INNER JOIN theme AS t 
                ON r.theme_id = t.id 
                WHERE r.date = ? AND rt.id = ? AND t.id = ?
                """;
        final Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(result);
    }

    public boolean checkExistReservationByTheme(final long themeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        final Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
        return Boolean.TRUE.equals(result);
    }

    public boolean checkExistReservationByTime(final long timeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        final Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
        return Boolean.TRUE.equals(result);
    }
}
