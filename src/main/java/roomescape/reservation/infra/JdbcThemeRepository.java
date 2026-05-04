package roomescape.reservation.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url")
            );

    @Override
    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme(name, description, thumbnail_url) VALUES (:name, :description, :thumbnail_url)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail_url", theme.getThumbnailUrl());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder);

        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("theme 저장 후 생성된 ID를 반환받지 못했습니다.");
        }

        return new Theme(
                keyHolder.getKey().longValue(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl()
        );
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        template.update(sql, params);
    }

    @Override
    public List<Theme> findByDate(LocalDate date) {
        String sql = "SELECT DISTINCT t.id, t.name, t.description, t.thumbnail_url " +
                "FROM theme t " +
                "JOIN schedule s ON t.id = s.theme_id " +
                "WHERE s.date = :date " +
                "ORDER BY t.id ASC";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date);

        return template.query(sql, params, themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(long id) {
        String sql = "SELECT * FROM theme WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Theme> result = template.query(sql, params, themeRowMapper);

        return result.stream().findFirst();
    }
}
