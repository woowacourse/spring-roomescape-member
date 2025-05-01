package roomescape.theme.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.ReservationThemeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationThemeRepository implements ReservationThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationThemeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationThemeEntity save(ReservationThemeEntity entity) {
        String query = "insert into theme (name, description, thumbnail) values (:name, :description, :thumbnail)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("thumbnail", entity.getThumbnail());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new ReservationThemeEntity(
                id,
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }

    @Override
    public List<ReservationThemeEntity> findAll() {
        String query = "select * from theme";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");
            return new ReservationThemeEntity(
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
    public Optional<ReservationThemeEntity> findById(Long id) {
        String query = "select * from theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            ReservationThemeEntity themeEntity = jdbcTemplate.queryForObject(query, params, (resultSet, rowNum) -> {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String thumbnail = resultSet.getString("thumbnail");
                return new ReservationThemeEntity(
                        id,
                        name,
                        description,
                        thumbnail
                );
            });
            return Optional.of(themeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReservationThemeEntity> findByName(String name) {
        String query = "SELECT * FROM theme WHERE name = :name";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name);
        try {
            ReservationThemeEntity themeEntity = jdbcTemplate.queryForObject(query, param, (resultSet, rowNum) -> {
                final long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String thumbnail = resultSet.getString("thumbnail");
                return new ReservationThemeEntity(
                        id,
                        name,
                        description,
                        thumbnail
                );
            });
            return Optional.of(themeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationThemeEntity> findPopularDescendingUpTo(final int limit) {
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
                    WHERE date >= DATEADD(DAY, -7, CURRENT_DATE)
                    GROUP BY theme_id
                ) as r_stats
                ON t.id = r_stats.theme_id
                ORDER BY cnt DESC
                FETCH FIRST :limit ROWS ONLY
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("limit", limit);
        return jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
            long id = resultSet.getLong("theme_id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");
            return new ReservationThemeEntity(
                    id,
                    name,
                    description,
                    thumbnail
            );
        });
    }
}
