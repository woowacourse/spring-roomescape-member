package roomescape.theme.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.jdbc.JdbcUtils;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infrastructure.entity.ThemeDBEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class H2ThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ThemeDBEntity> themeMapper = (resultSet, rowNum) ->
            ThemeDBEntity.of(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    @Override
    public boolean existsById(final ThemeId id) {
        final String sql = """
                select exists
                    (select 1 from theme where id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, id.getValue()));
    }

    @Override
    public boolean existsByName(final ThemeName name) {
        final String sql = """
                select exists
                    (select 1 from theme where name = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, name.getValue()));
    }

    @Override
    public List<Theme> findAll() {
        final String sql = """
                select id, name, description, thumbnail from theme
                """;

        return jdbcTemplate.query(sql, themeMapper).stream()
                .map(ThemeDBEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Theme> findById(final ThemeId id) {
        final String sql = """
                select id, name, description, thumbnail from theme where id = ?
                """;

        return JdbcUtils.queryForOptional(jdbcTemplate, sql, themeMapper, id.getValue())
                .map(ThemeDBEntity::toDomain);
    }

    @Override
    public Theme save(final Theme theme) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = """
                insert into theme (name, description, thumbnail) values (?, ?, ?)
                """;

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, theme.getName().getValue());
            preparedStatement.setString(2, theme.getDescription().getValue());
            preparedStatement.setString(3, String.valueOf(theme.getThumbnail().getValue()));

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
