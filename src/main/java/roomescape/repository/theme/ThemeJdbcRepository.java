package roomescape.repository.theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.entity.ThemeEntity;

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
    public ThemeEntity add(Theme theme) {
        Map<String, String> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("thumbnail", theme.getThumbnail());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ThemeEntity.of(id, theme);
    }

    @Override
    public List<ThemeEntity> findAll() {
        String sql = "select * from theme";
        List<ThemeEntity> themes = jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> {
                    ThemeEntity theme = new ThemeEntity(
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

    @Override
    public Optional<ThemeEntity> findById(Long id) {
        String sql = "select * from theme where id=?";
        List<ThemeEntity> theme = jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) ->
                        new ThemeEntity(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail")
                        ),
                id
        );
        return theme.stream().findFirst();
    }
}
