package roomescape.theme.infra;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .description(resultSet.getString("description"))
            .thumbnailImageUrl(resultSet.getString("thumbnail_image_url"))
            .durationTime(resultSet.getTime("duration_time").toLocalTime())
            .build();

    @Override
    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme(name, thumbnail_image_url, description, duration_time) "
                + "VALUES(:name, :thumbnailImageUrl, :description, :durationTime)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("thumbnailImageUrl", theme.getThumbnailImageUrl())
                .addValue("description", theme.getDescription())
                .addValue("durationTime", theme.getDurationTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder);
        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        theme.setId(generatedId);

        return theme;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }

    @Override
    public boolean existsThemeById(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM theme WHERE id=:id)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("id", id), Boolean.class));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail_image_url, duration_time FROM theme WHERE id = :id";

        List<Theme> themes = jdbcTemplate.query(sql, Map.of("id", id), rowMapper);
        return themes.stream().findFirst();
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_image_url, duration_time FROM theme";

        return jdbcTemplate.query(sql, Map.of(), rowMapper);
    }
}
