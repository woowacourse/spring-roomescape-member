package roomescape.dao;

import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationThemeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationThemeDao implements ReservationThemeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationThemeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationThemeEntity save(ReservationThemeEntity entity) {
        String query = "insert into reservation_theme (name, description, thumbnail) values (:name, :description, :thumbnail)";
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
        String query = "select * from reservation_theme";
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
        String query = "delete from reservation_theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        final int updated = jdbcTemplate.update(query, params);
        return updated > 0;
    }

    @Override
    public Optional<ReservationThemeEntity> findById(Long id) {
        String query = "select * from reservation_theme where id = :id";
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
        } catch (IncorrectResultSetColumnCountException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReservationThemeEntity> findByName(String name) {
        String query = "SELECT * FROM reservation_time WHERE name = :name";
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
        } catch (IncorrectResultSetColumnCountException e) {
            return Optional.empty();
        }
    }
}
