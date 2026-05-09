package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.projection.PopularThemeProjection;
import roomescape.repository.projection.ThemeEntity;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<ThemeEntity> ROW_MAPPER = (rs, rowNum) -> new ThemeEntity(
            rs.getLong("id"),
            new Theme(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail_url")
            )
    );

    private static final RowMapper<PopularThemeProjection> POPULAR_ROW_MAPPER = (rs, rowNum) -> new PopularThemeProjection(
            new ThemeEntity(
                    rs.getLong("id"),
                    new Theme(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("thumbnail_url")
                    )
            ),
            rs.getLong("reservation_count")
    );


    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ThemeEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, description, thumbnail_url FROM theme",
                ROW_MAPPER
        );
    }

    @Override
    public Optional<ThemeEntity> findById(Long id) {
        List<ThemeEntity> result = jdbcTemplate.query(
                "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?",
                ROW_MAPPER,
                id
        );
        return result.stream().findFirst();
    }

    @Override
    public ThemeEntity save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailUrl());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ThemeEntity(id, theme);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    @Override
    public List<PopularThemeProjection> findPopularBetween(LocalDate from, LocalDate to, int limit) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail_url,
                       COUNT(r.id) AS reservation_count
                FROM theme t
                INNER JOIN reservation r ON t.id = r.theme_id
                WHERE r.date >= ?
                  AND r.date <  ?
                GROUP BY t.id, t.name, t.description, t.thumbnail_url
                ORDER BY reservation_count DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, POPULAR_ROW_MAPPER,
                Date.valueOf(from), Date.valueOf(to), limit);
    }

}
