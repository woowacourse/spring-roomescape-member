package roomescape.reservation.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

import javax.sql.DataSource;
import java.util.List;

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
        final String sql = "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, " +
                "rt.id AS time_id, rt.start_at AS time_value, " +
                "t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time rt ON r.time_id = rt.id " +
                "INNER JOIN theme t ON r.theme_id = t.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Reservation findById(final long id) {
        final String sql = "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, " +
                "rt.id AS time_id, rt.start_at AS time_value, " +
                "t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time rt ON r.time_id = rt.id " +
                "INNER JOIN theme t ON r.theme_id = t.id " +
                "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
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

    public boolean checkReservationExists(final String date, final String time) {
        String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS t " +
                "ON r.time_id = t.id " +
                "WHERE r.date = ? AND t.start_at = ?";

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, time);
        return Boolean.TRUE.equals(result);
    }
}
