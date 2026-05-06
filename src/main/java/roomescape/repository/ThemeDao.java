package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.PopularThemeResponse;

@Component
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
                select t.id, t.name, t.description, t.thumbnail_image_url, count(r.id) as reservation_count
                from theme t
                join reservation r on t.id = r.theme_id and r.date >= ? and r.date <= ?
                group by t.id
                order by reservation_count desc, t.id asc
                limit ?
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
