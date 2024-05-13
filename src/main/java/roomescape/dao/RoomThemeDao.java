package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.RoomTheme;

@Repository
public class RoomThemeDao {
    private static final RowMapper<RoomTheme> MAPPER = (rs, rowNum) -> new RoomTheme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public RoomThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<RoomTheme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme", MAPPER);
    }

    public List<RoomTheme> findAllRanking() {
        String sql = """
                select t.id, t.name, t.description, t.thumbnail from theme as t
                inner join reservation as r on r.theme_id = t.id
                WHERE r.date > (NOW() -  8) AND r.date < NOW()
                group by t.id
                order by count(t.id) desc
                limit 10
                """;
        return jdbcTemplate.query(sql, MAPPER);
    }

    public RoomTheme findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM theme WHERE id = ?", MAPPER, id);
    }

    public RoomTheme save(RoomTheme roomTheme) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", roomTheme.getName())
                .addValue("description", roomTheme.getDescription())
                .addValue("thumbnail", roomTheme.getThumbnail());
        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new RoomTheme(id, roomTheme);
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id) > 0;
    }
}
