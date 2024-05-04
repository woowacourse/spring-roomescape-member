package roomescape.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper;

    public ReservationDao(
            final DataSource dataSource,
            final RowMapper<Reservation> reservationRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
        this.reservationRowMapper = reservationRowMapper;
    }

    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("NAME", reservation.getName().getValue())
                .addValue("DATE", reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addValue("TIME_ID", reservation.getTimeId())
                .addValue("THEME_ID", reservation.getThemeId());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    public Optional<Reservation> findById(long id) {
        try {
            String sql = """
                    SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                          theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                    FROM reservation AS r
                    INNER JOIN reservation_time AS time ON r.time_id = time.id
                    INNER JOIN theme ON r.theme_id = theme.id
                    WHERE r.id = ?
                    """;
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
    
    public List<Reservation> getAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                    theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> findByTimeId(final long timeId) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id 
                INNER JOIN theme ON r.theme_id = theme.id
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, timeId);
    }

    public List<Reservation> findByThemeId(final long themeId) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id 
                INNER JOIN theme ON r.theme_id = theme.id
                WHERE r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, themeId);
    }


    public List<Long> findByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date.toString(), timeId, themeId
        );
    }

    public List<Long> findTimeIdsByDateAndThemeId(LocalDate date, long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date.toString(), themeId
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

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", Long.valueOf(id));
    }
}
