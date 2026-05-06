package roomescape.repository.jdbc;

import static roomescape.repository.jdbc.ThemeEntityMapper.THEME_MAPPER;

import java.sql.PreparedStatement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isActiveByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 FROM theme WHERE name=? and is_active=1)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, name);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO theme (name, description, thumbnail_image_url, is_active) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailImageUrl());
            ps.setBoolean(4, theme.isActive());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Theme(id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl(),
                theme.isActive());
    }

    @Override
    public void update(Theme theme) {
        String sql = """
            UPDATE theme
            SET name = ?, description = ?, thumbnail_image_url = ?, is_active = ?
            WHERE id=?
        """;
        System.out.println("dddddd -> " + theme.getId());
        jdbcTemplate.update(sql,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl(),
                theme.isActive(),
                theme.getId()
        );
    }

    @Override
    public Optional<Theme> findById(long id) {
        try {
            String sql = "SELECT * FROM theme WHERE id = ?";
            Theme theme = jdbcTemplate.queryForObject(sql, THEME_MAPPER, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
