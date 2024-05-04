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

import roomescape.reservation.domain.Reservation;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper;

    public ReservationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final RowMapper<Reservation> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public List<Reservation> findAll() {
        final String sql = """ 
                SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, 
                       rt.id AS time_id, rt.start_at AS time_value, 
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail 
                FROM reservation r 
                INNER JOIN reservation_time rt ON r.time_id = rt.id 
                INNER JOIN theme t ON r.theme_id = t.id 
                ORDER BY  r.date ASC, rt.start_at ASC;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public long save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public int deleteById(final long id) {
        final String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean checkExistReservationOf(final LocalDate date, final long timeId, final long themeId) {
        String sql = """
                SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END 
                FROM reservation AS r 
                INNER JOIN reservation_time AS rt 
                ON r.time_id = rt.id 
                INNER JOIN theme AS t 
                ON r.theme_id = t.id 
                WHERE r.date = ? AND rt.id = ? AND t.id = ?
                """;

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(result);
    }
}
