package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail_url FROM theme",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail_url")
                ));
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                conn -> {
                    PreparedStatement preparedStatement = conn.prepareStatement(
                            "INSERT INTO theme(name, description, thumbnail_url) " +
                                    "VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, theme.name());
                    preparedStatement.setString(2, theme.description());
                    preparedStatement.setString(3, theme.thumbnailUrl());

                    return preparedStatement;
                },
                keyHolder);

        return new Theme(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(
                    "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?",
                    (rs, rowNum) -> new Theme(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("thumbnail_url")
                    ),
                    id);

            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int limit) {
        return jdbcTemplate.query(
                "SELECT th.id, th.name, th.description, th.thumbnail_url " +
                        "FROM reservation r JOIN theme th ON r.theme_id = th.id " +
                        "WHERE r.date BETWEEN ? AND ? " +
                        "GROUP BY th.id, th.name, th.description, th.thumbnail_url " +
                        "ORDER BY COUNT(r.id) DESC " +
                        "LIMIT ?",
                (rs, rowNum) -> {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String thumbnailUrl = rs.getString("thumbnail_url");
                    return new Theme(id, name, description, thumbnailUrl);
                },
                startInclusive, endInclusive, limit
        );
    }
}
