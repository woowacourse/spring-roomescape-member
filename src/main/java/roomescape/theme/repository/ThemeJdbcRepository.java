package roomescape.theme.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {
    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNumber) ->
            Theme.createWithId(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

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
        return Theme.createWithId(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return jdbcTemplate.query(
                sql,
                ROW_MAPPER
        );
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE from theme where id=?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id=?";
        List<Theme> theme = jdbcTemplate.query(
                sql,
                ROW_MAPPER,
                id
        );
        return theme.stream().findFirst();
    }
}
