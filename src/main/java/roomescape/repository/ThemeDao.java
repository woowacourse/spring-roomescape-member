package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;
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
}
