package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;

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
                    r.date, 
                    t.id as time_id, 
                    t.start_at as time_value, 
                    tm.id as theme_id, 
                    tm.name as theme_name, 
                    tm.description,     
                    tm.thumbnail, 
                    m.id as member_id,
                    m.name as name,
                    m.role,
                    m.email,
                    m.password
                    
                FROM reservation AS r 
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id 
                INNER JOIN theme AS tm 
                ON r.theme_id = tm.id
                INNER JOIN member AS m 
                ON r.member_id = m.id
        """;
        return jdbcTemplate.query(findAllSql,getReservationRowMapper());
    }

    public Long insert(String date, Long timeId, Long themeId, Long memberId) {
        String insertSql = "INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, date);
            ps.setLong(2, timeId);
            ps.setLong(3, themeId);
            ps.setLong(4, memberId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String deleteFromIdSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteFromIdSql, id);
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

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, numRow) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("date"),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("time_value")
                ),
                new ReservationTheme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("role"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                )
        );
    }
}
