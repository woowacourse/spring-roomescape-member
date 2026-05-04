package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail_img_url", theme.getThumbnailImgUrl());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.create(id, theme);
    }

    @Override
    public Boolean existsByNameAndDescription(Theme theme) {
        String sql = "SELECT EXISTS(SELECT 1 FROM theme WHERE name = ? AND description = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, theme.getName(), theme.getDescription());
    }

    @Override
    public Integer delete(long id) {
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
