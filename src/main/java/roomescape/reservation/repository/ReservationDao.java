package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper;

    public ReservationDao(
            final DataSource dataSource,
            final NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            final RowMapper<Reservation> reservationRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.reservationRowMapper = reservationRowMapper;
    }

    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(reservation);
        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    public List<Reservation> getAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                    r.date,
                    time.id AS time_id, time.start_at AS time_value,
                    theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                INNER JOIN member AS m ON r.member_id = m.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<Reservation> findById(final long id) {
        try {
            String sql = """
                    SELECT
                        r.id AS reservation_id,
                        m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                        r.date,
                        time.id AS time_id, time.start_at AS time_value,
                        theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                    FROM reservation AS r
                    INNER JOIN reservation_time AS time ON r.time_id = time.id
                    INNER JOIN theme ON r.theme_id = theme.id
                    INNER JOIN member AS m ON r.member_id = m.id
                    WHERE r.id = ?
                    """;
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }


    public List<Reservation> findByMemberId(final Long memberId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                    r.date,
                    time.id AS time_id, time.start_at AS time_value,
                    theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.member_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, memberId);
    }

    public List<Reservation> findByTimeId(final long timeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                    r.date,
                    time.id AS time_id, time.start_at AS time_value,
                    theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, timeId);
    }

    public List<Reservation> findByThemeId(final long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                    r.date,
                    time.id AS time_id, time.start_at AS time_value,
                    theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, themeId);
    }

    public List<Reservation> findByDateFromAndDateTo(final LocalDate dateFrom, final LocalDate dateTo) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.member_name, m.email, m.password, m.member_role,
                    r.date,
                    time.id AS time_id, time.start_at AS time_value,
                    theme.id AS theme_id, theme.theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE (:date_from IS NULL AND :date_to IS NULL)
                    OR (:date_from IS NOT NULL AND :date_to IS NULL AND r.date > :date_from)
                    OR (:date_from IS NULL AND :date_to IS NOT NULL AND r.date < :date_to)
                    OR (:date_from IS NOT NULL AND :date_to IS NOT NULL AND r.date BETWEEN :date_from AND :date_to)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("date_from", dateFrom);
        params.addValue("date_to", dateTo);
        return namedParameterJdbcTemplate.query(sql, params, reservationRowMapper);
    }

    public List<Long> findByDateAndTimeIdAndThemeId(final LocalDate date, final long timeId, final long themeId) {
        String sql = "SELECT id FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("id"),
                date, timeId, themeId
        );
    }

    public List<Long> findTimeIdsByDateAndThemeId(final LocalDate date, final long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("id"),
                date, themeId
        );
    }

    public List<Long> findRanking(final LocalDate from, final LocalDate to, final int count) {
        String sql = """
                SELECT theme_id, count(*) AS count FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY count DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("theme_id"),
                from, to, count
        );
    }

    public int delete(final long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", Long.valueOf(id));
    }
}
