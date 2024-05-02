package roomescape.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        return jdbcTemplate.query(findAllSql,getReservationRowMapper());
    }

    public Long insert(String name, String date, Long timeId, Long themeId) {
        String insertSql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String deleteFromIdSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteFromIdSql, id);
    }

    public Optional<Reservation> findById(Long id) {
        String findByIdSql = """
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
        List<Reservation> reservations = jdbcTemplate.query(findByIdSql, getReservationRowMapper(), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(reservations));
    }

    public int countByTimeId(Long timeId) {
        String findByIdSql = "SELECT count(*) FROM reservation WHERE time_id =?";
        return jdbcTemplate.queryForObject(findByIdSql, Integer.class, timeId);
    }

    public int countByThemeId(Long themeId) {
        String findByIdSql = "SELECT count(*) FROM reservation WHERE theme_id =?";
        return jdbcTemplate.queryForObject(findByIdSql, Integer.class, themeId);
    }

    public int count(String date, Long timeId, Long themeId) {
        String findByIdSql = "SELECT count(*) FROM reservation WHERE time_id = ? AND theme_id = ? AND date = ?";
        return jdbcTemplate.queryForObject(findByIdSql, Integer.class, timeId, themeId, date);
    }

    public List<Long> findBestThemeIdInWeek(String from, String to) {
        String findBestThemeIdSql = "SELECT theme_id, count(*) AS total FROM reservation WHERE date BETWEEN ? AND ? GROUP BY theme_id ORDER BY total DESC";
        return jdbcTemplate.query(findBestThemeIdSql, (rs, rowNum) -> rs.getLong("theme_id"), from, to);
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
