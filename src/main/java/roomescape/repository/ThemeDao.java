package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.PopularThemeResponse;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> {
        Theme theme = new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_image_url")
        );
        return theme;
    };

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String sql = "select id, name, description, thumbnail_image_url from theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Theme findById(Long id) {
        String sql = "select id, name, description, thumbnail_image_url from theme where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long save(CreateThemeRequest request) {
        String sql = "insert into theme(name, description, thumbnail_image_url) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, request.name());
            ps.setString(2, request.description());
            ps.setString(3, request.thumbnailImageUrl());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<PopularThemeResponse> findPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail_image_url, stats.reservation_count
                FROM theme t
                JOIN (
                    SELECT theme_id, COUNT(id) AS reservation_count
                    FROM reservation
                    WHERE date >= ? AND date <= ?
                    GROUP BY theme_id
                ) stats ON t.id = stats.theme_id
                ORDER BY stats.reservation_count DESC, t.id ASC
                LIMIT ?
                """;

        RowMapper<PopularThemeResponse> popularThemeRowMapper = (resultSet, rowNum) -> new PopularThemeResponse(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_image_url"),
                resultSet.getLong("reservation_count")
        );

        return jdbcTemplate.query(sql, popularThemeRowMapper, startDate, endDate, limit);
    }
}
