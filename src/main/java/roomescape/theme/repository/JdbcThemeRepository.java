package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Theme save(Theme theme) {
        String query = "insert into theme (name, description, thumbnail) values (:name, :description, :thumbnail)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(query, params, keyHolder);

        return new Theme(
                keyHolder.getKey().longValue(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    @Override
    public List<Theme> findAll() {
        String query = "select * from theme";

        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");

            return new Theme(
                    id,
                    name,
                    description,
                    thumbnail
            );
        });
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "delete from theme where id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        final int updated = jdbcTemplate.update(query, params);

        return updated > 0;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            Theme theme = jdbcTemplate.queryForObject(query, params, (resultSet, rowNum) -> {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String thumbnail = resultSet.getString("thumbnail");

                return new Theme(
                        id,
                        name,
                        description,
                        thumbnail
                );
            });
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Theme> findByName(String name) {
        String query = "SELECT * FROM theme WHERE name = :name";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name);

        try {
            Theme theme = jdbcTemplate.queryForObject(query, param, (resultSet, rowNum) -> {
                final long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String thumbnail = resultSet.getString("thumbnail");

                return new Theme(
                        id,
                        name,
                        description,
                        thumbnail
                );
            });
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findPopularDescendingUpTo(
            LocalDate startDate,
            LocalDate endDate,
            final int limit
    ) {
        String query = """
                SELECT
                    t.id as theme_id,
                    t.name,
                    t.description,
                    t.thumbnail,
                    r_stats.cnt
                FROM theme as t
                LEFT JOIN (
                    SELECT
                        theme_id,
                        COUNT(*) as cnt
                    FROM reservation
                    WHERE date BETWEEN :startDate AND :endDate
                    GROUP BY theme_id
                ) as r_stats
                ON t.id = r_stats.theme_id
                ORDER BY cnt DESC, theme_id DESC
                FETCH FIRST :limit ROWS ONLY
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("limit", limit);

        return jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
            long id = resultSet.getLong("theme_id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");

            return new Theme(
                    id,
                    name,
                    description,
                    thumbnail
            );
        });
    }
}
