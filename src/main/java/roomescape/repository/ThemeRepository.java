package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.time.Duration;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.of(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("image_url"),
            resultSet.getTime("start_at").toLocalTime(),
            resultSet.getTime("finish_at").toLocalTime(),
            Duration.ofHours(resultSet.getLong("play_time"))
    );

    public Theme save(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl())
                .addValue("start_at", theme.getStartAt())
                .addValue("finish_at", theme.getFinishAt())
                .addValue("play_time", theme.getPlayTime());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getImageUrl(), theme.getStartAt(), theme.getFinishAt(), theme.getPlayTime());
    }

    public void deleteById(Long id) {
        String query = "delete from theme where id = ?";
        jdbcTemplate.update(query, id);
    }

    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = ?";
        return jdbcTemplate.query(query, rowMapper, id)
                .stream()
                .findFirst();
    }

    public List<Theme> findAll() {
        String query = "select * from theme";
        return jdbcTemplate.query(query, rowMapper);
    }
}
