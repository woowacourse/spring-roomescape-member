package roomescape.reservation.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReservationDao {
    private static final RowMapper<Reservation> rowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("reservation_name"),
                    resultSet.getString("date"),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getString("time_value")
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    ));
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
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

    public boolean checkReservationExists(final String date, final long timeId, final long themeId) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS rt " +
                "ON r.time_id = rt.id " +
                "INNER JOIN theme AS t " +
                "ON r.theme_id = t.id " +
                "WHERE r.date = ? AND rt.id = ? AND t.id = ?";

        final Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(result);
    }
}
