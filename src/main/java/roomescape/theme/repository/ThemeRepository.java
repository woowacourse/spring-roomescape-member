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
                            ORDER BY COUNT(r.id) DESC
                            LIMIT ?
                            """;
        return jdbcTemplate.query(sql, themeRowMapper, from, to, size);
    }

    public long save(Theme theme) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    MERGE INTO theme t
                    USING (
                        VALUES (?, ?, ?, ?)
                    ) s(id, name, description, thumbnail_url)
                    ON t.id = s.id
                    WHEN MATCHED THEN
                        UPDATE SET
                            name = s.name,
                            description = s.description,
                            thumbnail_url = s.thumbnail_url
                    WHEN NOT MATCHED THEN
                        INSERT (name, description, thumbnail_url)
                        VALUES (s.name, s.description, s.thumbnail_url)
                    """,
                    new String[]{"id"}
            );

            ps.setObject(1, theme.getId());
            ps.setString(2, theme.getName());
            ps.setString(3, theme.getDescription());
            ps.setString(4, theme.getThumbnailUrl());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        }

        return theme.getId();
    }

    public void remove(long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
