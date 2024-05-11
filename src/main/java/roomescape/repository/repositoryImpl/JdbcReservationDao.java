package roomescape.repository.repositoryImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.repository.ReservationDao;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getTime("time_value").toLocalTime()),
            new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail")),
            new Member(resultSet.getLong("member_id"), resultSet.getString("member_name"),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password"), Role.from(resultSet.getString("member_role"))));

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id as reservation_id,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail,
                m.id as member_id,
                m.name as member_name,
                m.email as member_email,
                m.password as member_password,
                m.role as member_role
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                INNER JOIN member as m on r.member_id = m.id
                """, reservationRowMapper);
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findAll(final Long memberId, final Long themeId,
                                     final LocalDate dateFrom,
                                     final LocalDate dateTo) {
        String sql = """
                SELECT
                r.id as reservation_id,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail,
                m.id as member_id,
                m.name as member_name,
                m.email as member_email,
                m.password as member_password,
                m.role as member_role
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                INNER JOIN member as m on r.member_id = m.id
                WHERE member_id = ? AND theme_id = ? AND date > ? AND date < ?
                """;
        return Collections.unmodifiableList(jdbcTemplate.query(sql, reservationRowMapper, memberId, themeId, dateFrom, dateTo));
    }

    @Override
    public Reservation findById(final long id) {
        String sql = """
                SELECT
                r.id as reservation_id,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail,
                m.id as member_id,
                m.name as member_name,
                m.email as member_email,
                m.password as member_password,
                m.role as member_role
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                INNER JOIN member as m on r.member_id = m.id
                WHERE r.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    @Override
    public boolean existByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        int count = jdbcTemplate.queryForObject("""
                SELECT count(*) 
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id
                WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?
                """, Integer.class, date, timeId, themeId);
        return count > 0;
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservation);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        return findById(newId.longValue());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    @Override
    public List<Long> findTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return Collections.unmodifiableList(jdbcTemplate.queryForList("SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?", Long.class,
                date, themeId));
    }

    @Override
    public boolean existByTimeId(final Long id) {
        String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }

    @Override
    public boolean existByThemeId(final Long id) {
        String sql = "SELECT count(*) FROM reservation WHERE theme_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }
}
