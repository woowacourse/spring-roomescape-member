package roomescape.persistence.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.Theme;
import roomescape.persistence.entity.ThemeEntity;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Theme theme) {
        final ThemeEntity themeEntity = ThemeEntity.from(theme);
        final String sql = "INSERT INTO THEME (name, description, thumbnail) values (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, themeEntity.name());
            ps.setString(2, themeEntity.description());
            ps.setString(3, themeEntity.thumbnail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Theme> find(final Long id) {
        final String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        try {
            final ThemeEntity themeEntity = jdbcTemplate.queryForObject(sql, ThemeEntity.getDefaultRowMapper(), id);
            return Optional.of(themeEntity.toDomain());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "SELECT id, name, description, thumbnail FROM theme";

        return jdbcTemplate.query(sql, ThemeEntity.getDefaultRowMapper()).stream()
                .map(ThemeEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        final String sql = "DELETE FROM theme WHERE id = ?";
        final int rowNum = jdbcTemplate.update(sql, id);

        return rowNum == 1;
    }

    @Override
    public List<Theme> findPopularThemesBetween(
            final String startDate,
            final String endDate
    ) {
        final String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail,
                    COUNT(r.id) AS reservation_count
                FROM theme AS t
                LEFT JOIN reservation AS r
                    ON t.id = r.theme_id
                    AND r.date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY reservation_count DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ), startDate, endDate
        );
    }
}
