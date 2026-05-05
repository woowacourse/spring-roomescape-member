package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> {
        return new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail_image_url")
        );
    };

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_image_url " +
                "FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Long save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail_image_url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailImageUrl());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        List<Theme> results = jdbcTemplate.query(sql, themeRowMapper, id);
        return results.stream().findFirst();
    }
}
