package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.entity.ThemeEntity;

@RequiredArgsConstructor
@Repository
public class H2ThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(final ThemeEntity themeEntity) {
        final String sql = """
                INSERT INTO themes (name, description, thumbnail) VALUES (?, ?, ?)
                """;
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        final int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, themeEntity.name());
            ps.setString(2, themeEntity.description());
            ps.setString(3, themeEntity.thumbnail());
            return ps;
        }, keyHolder);

        if (rowAffected != 1) {
            throw new IllegalStateException("테마 저장에 실패했습니다.");
        }

        final Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("생성된 키가 존재하지 않습니다.");
        }

        return key.longValue();
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = """
                SELECT
                    id,
                    name,
                    description,
                    thumbnail
                FROM themes 
                WHERE id = ?
                """;
        final List<Theme> themes = jdbcTemplate.query(sql, THEME_ROW_MAPPER, id);

        if (!themes.isEmpty()) {
            return Optional.of(themes.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public long countByName(final String name) {
        final String sql = """
                SELECT COUNT(*) AS count
                FROM themes
                WHERE name = ? 
                """;

        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }

    @Override
    public List<Theme> findAll() {
        final String sql = """
                SELECT
                    id,
                    name,
                    description,
                    thumbnail
                FROM themes 
                """;

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = """
                DELETE FROM themes 
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
