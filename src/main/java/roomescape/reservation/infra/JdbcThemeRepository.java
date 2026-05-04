package roomescape.reservation.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Theme;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {
    private final NamedParameterJdbcTemplate template;

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
}
