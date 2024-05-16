package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDAO(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation insert(final Reservation reservation) {
        final LocalDate date = reservation.getDate();
        final ReservationTime time = reservation.getTime();
        final Theme theme = reservation.getTheme();
        final Member member = reservation.getMember();

        final SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id", member.getId())
                .addValue("date", date)
                .addValue("time_id", time.getId())
                .addValue("theme_id", theme.getId());

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Reservation(id, date, member, time, theme);
    }

    public List<Reservation> selectAll() {
        final String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.date,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email,
                    m.password,
                    rt.id AS time_id,
                    rt.start_at AS time_value,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id 
                INNER JOIN theme AS t 
                ON r.theme_id = t.id 
                INNER JOIN member AS m 
                ON r.member_id = m.id;
                """;

        return jdbcTemplate.query(sql, reservationRowMapper());
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean existReservationTime(final Long timeId) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public Boolean existTheme(final Long themeId) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    public Boolean existReservationOf(final LocalDate date, final ReservationTime time) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ? AND time_id = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, time.getId());
    }

    public List<Reservation> findReservations(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        final String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.date,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email,
                    m.password,
                    rt.id AS time_id,
                    rt.start_at AS time_value,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id 
                INNER JOIN theme AS t 
                ON r.theme_id = t.id 
                INNER JOIN member AS m 
                ON r.member_id = m.id
                WHERE theme_id = ? AND member_id = ? AND date BETWEEN ? AND ?;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper(), themeId, memberId, dateFrom, dateTo);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getDate("date").toLocalDate(),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
    }
}
