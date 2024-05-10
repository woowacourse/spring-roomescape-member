package roomescape.repository.roomescape;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.roomescape.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper;

    public ThemeDao(
            final DataSource dataSource,
            final RowMapper<Theme> themeRowMapper
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.themeRowMapper = themeRowMapper;
    }

    public Theme save(final Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO theme(theme_name, description, thumbnail) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sql, new String[]{"id"}
                );
                ps.setString(1, theme.getThemeNameValue());
                ps.setString(2, theme.getDescription());
                ps.setString(3, theme.getThumbnail());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
            return theme.assignId(id);
        } catch (final DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Theme> getAll() {
        return jdbcTemplate.query(
                "SELECT id AS theme_id, theme_name, description, thumbnail FROM theme",
                themeRowMapper
        );
    }

    public Optional<Theme> findById(final long id) {
        try {
            String sql = "SELECT id AS theme_id, theme_name, description, thumbnail FROM theme WHERE id = ? ";
            return Optional.of(jdbcTemplate.queryForObject(sql, themeRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public int delete(final long id) {
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", Long.valueOf(id));
    }
}
