package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.Name;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.member.Role;
import roomescape.domain.theme.Theme;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationJdbcDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNumber) -> new Reservation(
            resultSet.getLong("reservation_id"),
            new Member(
                    resultSet.getLong("member_id"),
                    new Name(resultSet.getString("member_name")),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password"),
                    Role.valueOf(resultSet.getString("member_role"))
            ),
            resultSet.getString("date"),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getString("time_value")
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            ));

    public ReservationJdbcDao(final JdbcTemplate jdbcTemplate, final DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMemberId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId());
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(Objects.requireNonNull(id), reservation.getMember(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id AS reservation_id, r.member_id, r.date, 
                    t.id AS time_id, t.start_at AS time_value,
                    th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail,
                    m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Reservation> findAllByThemeAndMemberAndPeriod(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        final String sql = """
                SELECT r.id AS reservation_id, r.member_id, r.date, 
                    t.id AS time_id, t.start_at AS time_value,
                    th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail,
                    m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE th.id = ? AND m.id = ? AND (r.date BETWEEN ? AND ?);
                """;
        return jdbcTemplate.query(sql, rowMapper, themeId, memberId, dateFrom, dateTo);
    }

    @Override
    public List<Reservation> findAllByDateAndTimeAndThemeId(final LocalDate date, final ReservationTime time, final Long themeId) {
        final String sql = """
                SELECT 
                    r.id AS reservation_id, r.member_id, r.date, 
                    t.id AS time_id, t.start_at AS time_value,
                    th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail,
                    m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE `date` = ? AND t.start_at = ? AND th.id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, Date.valueOf(date), Time.valueOf(time.getStartAt()), themeId);
    }

    @Override
    public boolean existById(final Long id) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id = ?) AS is_exist";
        return jdbcTemplate.queryForObject(sql,
                (resultSet, rowNumber) -> resultSet.getBoolean("is_exist"), id);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int countByTimeId(final Long timeId) {
        final String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, timeId);
    }

    @Override
    public List<Long> findAllTimeIdsByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(sql,
                (resultSet, rowNumber) -> resultSet.getLong("time_id"),
                Date.valueOf(date), themeId);
    }
}
