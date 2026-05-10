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
import roomescape.dto.theme.PopularThemeResponse;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

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

    public ThemeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll(int limit, int offset) {
        String sql = """
                select id, name, description, thumbnail_image_url
                from theme
                order by id
                limit ? offset ?
                """;
        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    @Override
    public Theme findById(Long id) {
        String sql = "select id, name, description, thumbnail_image_url from theme where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public Long save(Theme theme) {
        String sql = "insert into theme(name, description, thumbnail_image_url) values(?, ?, ?)";

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

    @Override
    public void deleteById(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
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
