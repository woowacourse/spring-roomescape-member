package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String findAllSql = """
                        SELECT 
                            r.id as reservation_id, 
                            r.name,     
                            r.date, 
                            t.id as time_id, 
                            t.start_at as time_value, 
                            tm.id as theme_id, 
                            tm.name as theme_name, 
                            tm.description,     
                            tm.thumbnail 
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t 
                        ON r.time_id = t.id 
                        INNER JOIN theme AS tm 
                        ON r.theme_id = tm.id
                """;
        return jdbcTemplate.query(findAllSql, getReservationRowMapper());
    }

    public Long insert(String name, String date, Long timeId, Long themeId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);

        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                        SELECT 
                            r.id as reservation_id, 
                            r.name,     
                            r.date, 
                            t.id as time_id, 
                            t.start_at as time_value, 
                            tm.id as theme_id, 
                            tm.name as theme_name, 
                            tm.description,     
                            tm.thumbnail 
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t 
                        ON r.time_id = t.id 
                        INNER JOIN theme AS tm 
                        ON r.theme_id = tm.id
                        WHERE t.id = ?
                """;
        List<Reservation> reservations = jdbcTemplate.query(sql, getReservationRowMapper(), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(reservations));
    }

    public int countByTimeId(Long timeId) {
        String sql = "SELECT count(*) FROM reservation WHERE time_id =?";
        return jdbcTemplate.queryForObject(sql, Integer.class, timeId);
    }

    public int countByThemeId(Long themeId) {
        String sql = "SELECT count(*) FROM reservation WHERE theme_id =?";
        return jdbcTemplate.queryForObject(sql, Integer.class, themeId);
    }

    public int count(String date, Long timeId, Long themeId) {
        String sql = "SELECT count(*) FROM reservation WHERE time_id = ? AND theme_id = ? AND date = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, timeId, themeId, date);
    }

    public List<Long> findBestThemeIdInWeek(String from, String to) {
        String sql = "SELECT theme_id, count(*) AS total FROM reservation WHERE date BETWEEN ? AND ? GROUP BY theme_id ORDER BY total DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("theme_id"), from, to);
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, numRow) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("time_value")
                ),
                new ReservationTheme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }
}
