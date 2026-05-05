package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.entity.ThemeEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Theme> findById(final Long themeId) {
        final String sql = """
                SELECT id, name, description, thumbnail_url
                FROM theme
                WHERE id = ?
                """;

        try {
            final Theme theme = jdbcTemplate.queryForObject(
                    sql,
                    this::mapToDomain,
                    themeId
            );

            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Theme save(final Theme themeWithoutId) {
        final ThemeEntity themeEntity = toEntity(themeWithoutId);

        final long themeId = insertTheme(themeEntity);

        return themeWithoutId.saved(themeId);
    }

    public boolean deleteById(final Long themeId) {
        final String sql = """
                DELETE FROM theme
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, themeId) > 0;
    }


    private long insertTheme(final ThemeEntity themeEntity) {
        final String sql = """
                INSERT INTO theme (name, description, thumbnail_url)
                VALUES (?, ?, ?)
                """;

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, themeEntity.name());
            preparedStatement.setString(2, themeEntity.description());
            preparedStatement.setString(3, themeEntity.thumbnailUrl());

            return preparedStatement;
        }, keyHolder);

        return generatedIdFrom(keyHolder);
    }

    private static long generatedIdFrom(final KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("생성된 id를 가져오지 못했습니다.");
        }

        final long themeId = keyHolder.getKey().longValue();
        return themeId;
    }


    /**
     * 엔티티 - 도메인 매핑 메서드
     */
    private Theme mapToDomain(final ResultSet resultSet, final int rowNum) throws SQLException {
        return Theme.restore(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_url")
        );
    }

    private ThemeEntity toEntity(final Theme theme) {
        return new ThemeEntity(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl()
        );
    }
}
