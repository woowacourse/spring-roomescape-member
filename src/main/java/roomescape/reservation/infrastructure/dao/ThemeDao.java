package roomescape.reservation.infrastructure.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.DeleteThemeException;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;

@Repository
public class ThemeDao implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(ThemeRequest themeRequest) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("theme")
                .usingColumns("name", "description", "thumbnail")
                .usingGeneratedKeyColumns("id");

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", themeRequest.getName())
                .addValue("description", themeRequest.getDescription())
                .addValue("thumbnail", themeRequest.getThumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        ));
    }

    @Override
    public void delete(Long id) {
        String sql = """
                DELETE FROM theme WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, id);
        if (result != 1) {
            throw new DeleteThemeException("[ERROR] 삭제하지 못했습니다.");
        }
    }

    @Override
    public Optional<Theme> findById(Long themeId) {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> new Theme(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    )
                    , themeId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
