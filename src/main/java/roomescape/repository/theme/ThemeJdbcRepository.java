package roomescape.repository.theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme add(Theme theme) {
        Map<String, String> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("thumbnail", theme.getThumbnail());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return theme.withId(id);
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        List<Theme> themes = jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> {
                    Theme theme = new Theme(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    );
                    return theme;
                }
        );
        return themes;
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE from theme where id=?";
        return jdbcTemplate.update(sql, id);
    }
}
