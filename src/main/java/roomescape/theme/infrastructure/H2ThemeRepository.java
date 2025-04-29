package roomescape.theme.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.entity.ThemeEntity;
import roomescape.theme.domain.ThemeRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class H2ThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ThemeEntity> themeMapper = (resultSet, rowNum) ->
            ThemeEntity.of(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    @Override
    public List<Theme> findAll() {
        String sql = """
                select id, name, description, thumbnail from theme
                """;

        return jdbcTemplate.query(sql, themeMapper).stream()
                .map(ThemeConverter::toDomain)
                .toList();
    }
}
