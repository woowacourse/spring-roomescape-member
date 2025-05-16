package roomescape.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("theme")
            .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(query, mapToTheme());
        return themes;
    }

    public Theme save(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());

        long id = simpleJdbcInsert.executeAndReturnKeyHolder(parameters).getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public int deleteById(Long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * from theme where id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, mapToTheme(), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean getCountByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 from theme where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public List<Theme> findTop10() {
        String sql = """
            select count(*), t.id, t.name, t.description, t.thumbnail from theme as t
            left join (
                select *
                from reservation as r
                where PARSEDATETIME(r.date, 'yyyy-MM-dd')
                    between TIMESTAMPADD(DAY, -8, CURRENT_DATE)
                        and TIMESTAMPADD(DAY, -1, CURRENT_DATE)
                )
            as r on t.id = r.theme_id
            group by t.id
            order by count(*) desc
            limit 10
            """;
        return jdbcTemplate.query(sql, mapToTheme());
    }

    private RowMapper<Theme> mapToTheme() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");
            return new Theme(id, name, description, thumbnail);
        };
    }
}
