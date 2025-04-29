package roomescape.theme.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infrastructure.entity.ThemeEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

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
        final String sql = """
                select id, name, description, thumbnail from theme
                """;

        return jdbcTemplate.query(sql, themeMapper).stream()
                .map(ThemeConverter::toDomain)
                .toList();
    }

    @Override
    public Theme save(final Theme theme) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = """
                insert into theme values (?, ?, ?)
                """;

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theme.getName().getValue());
            preparedStatement.setString(2, theme.getDescription().getValue());
            preparedStatement.setString(3, theme.getThumbnail().getValue());

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return Theme.withId(
                ThemeId.from(generatedId),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }

    @Override
    public void deleteById(final ThemeId id) {
        final String sql = "delete from theme where id = ?";

        jdbcTemplate.update(sql, id.getValue());
    }
}
