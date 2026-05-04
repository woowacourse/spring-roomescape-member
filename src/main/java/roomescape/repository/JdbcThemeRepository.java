package roomescape.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        return List.of();
    }

    @Override
    public Theme findById(long id) {
        return null;
    }

    @Override
    public Theme save(Theme theme) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "name", theme.name(),
                "description", theme.description(),
                "thumbnail_url", theme.thumbnailUrl());

        long id = insert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme.name(), theme.description(), theme.thumbnailUrl());
    }

    @Override
    public void deleteById(long id) {

    }
}
