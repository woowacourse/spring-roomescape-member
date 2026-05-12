package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.doamin.Theme;

@Repository
@RequiredArgsConstructor
public class ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail_url")
    );

    public Optional<Theme> findById(long id) {
        try {
            final String sql = "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, themeRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query( "SELECT id, name, description, thumbnail_url FROM theme", themeRowMapper);
    }

    public List<Theme> findPopularThemes(int size, LocalDate from, LocalDate to) {
        final String sql = """
                            SELECT
                                th.id,
                                th.name,
                                th.description,
                                th.thumbnail_url
                            FROM reservation AS r
                            INNER JOIN theme AS th ON r.theme_id = th.id
                            WHERE r.date BETWEEN ? AND ?
                            GROUP BY
                                th.id,
                                th.name,
                                th.description,
                                th.thumbnail_url
                            ORDER BY COUNT(*) DESC
                            LIMIT ?
                            """;
        return jdbcTemplate.query(sql, themeRowMapper, from, to, size);
    }

    public Theme save(Theme theme) {
        if (theme.getId() == null) {
            return insert(theme);
        }

        return merge(theme);
    }

    private Theme merge(Theme theme) {
        final String sql = """
                UPDATE theme
                SET name = ?,
                    description = ?,
                    thumbnail_url = ?
                WHERE id = ?
                """;
        final int affectedRows = jdbcTemplate.update(
                sql,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl(),
                theme.getId()
        );

        if (affectedRows == 0) {
            return insert(theme);
        }

        return theme;
    }

    private Theme insert(Theme theme) {
        if (theme.getId() != null) {
            final String sql = "INSERT INTO theme (id, name, description, thumbnail_url) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
            return theme;
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                    new String[]{"id"}
            );

            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailUrl());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return new Theme(keyHolder.getKey().longValue(), theme.getName(), theme.getDescription(),
                    theme.getThumbnailUrl());
        }

        return theme;
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
