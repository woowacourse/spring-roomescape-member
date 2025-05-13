package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.Theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public Theme save(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());

        long id = simpleJdbcInsert.executeAndReturnKeyHolder(parameters).getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public int deleteById(Long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }

    @Override
    public Theme findById(Long id) {
        String sql = "SELECT * from theme where id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    @Override
    public boolean existByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 from theme where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    @Override
    public List<Theme> findPopular(int count) {
//        String sql = """
//                SELECT t.id, t.name, t.description, t.thumbnail
//                FROM theme AS t
//                LEFT JOIN (
//                    SELECT r.theme_id
//                    FROM reservation AS r
//                    WHERE PARSEDATETIME(r.date, 'yyyy-MM-dd') BETWEEN TIMESTAMPADD(DAY, -7, CURRENT_DATE) AND TIMESTAMPADD(DAY, -1, CURRENT_DATE)
//                ) AS r ON t.id = r.theme_id
//                GROUP BY t.id
//                ORDER BY count(*) DESC
//                limit ?
//                """;

        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail 
                FROM theme AS t
                LEFT JOIN reservation AS r ON t.id = r.theme_id
                    AND PARSEDATETIME(r.date, 'yyyy-MM-dd') BETWEEN TIMESTAMPADD(DAY, -7, CURRENT_DATE) AND TIMESTAMPADD(DAY, -1, CURRENT_DATE)
                GROUP BY t.id
                ORDER BY count(*) DESC
                limit ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, count);
    }
}
