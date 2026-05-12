package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.EntityId;
import roomescape.domain.Theme;
import roomescape.exception.DataReferencedException;

@Repository
public class ThemeRepository {

    private static final String FK_RESERVATION_THEME_ID = "fk_reservation_theme_id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme");
    }

    public Theme persist(Theme theme) {
        simpleJdbcInsert.execute(Map.of(
                "id", theme.id().getValueAsUuid(),
                "name", theme.name(),
                "description", theme.description(),
                "image_url", theme.imageUrl()
        ));

        return theme;
    }

    public List<Theme> findAll() {
        String findSql = "SELECT id, name, description, image_url"
                + " FROM theme";

        return namedParameterJdbcTemplate.query(findSql, themeRowMapper());
    }

    public Optional<Theme> findById(EntityId id) {
        try {
            String findSql = "SELECT id, name, description, image_url"
                    + " FROM theme"
                    + " WHERE id = :id";

            Theme theme = namedParameterJdbcTemplate.queryForObject(
                    findSql,
                    Map.of("id", id.getValueAsUuid()),
                    themeRowMapper()
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Map<EntityId, Theme> findByIds(Collection<EntityId> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }

        String findSql = "SELECT id, name, description, image_url"
                + " FROM theme"
                + " WHERE id IN (:ids)";
        List<UUID> jdbcIds = ids.stream()
                .map(EntityId::getValueAsUuid)
                .toList();

        List<Theme> themes = namedParameterJdbcTemplate.query(
                findSql,
                Map.of("ids", jdbcIds),
                themeRowMapper()
        );

        return themes.stream()
                .collect(Collectors.toMap(
                        Theme::id,
                        Function.identity()
                ));
    }

    public boolean delete(EntityId themeId) {
        try {
            String deleteSql = "DELETE FROM theme"
                    + " WHERE id = :id";

            int deletedRowCount = namedParameterJdbcTemplate.update(
                    deleteSql,
                    Map.of("id", themeId.getValueAsUuid())
            );

            return isDeleted(deletedRowCount);
        } catch (DataIntegrityViolationException exception) {
            if (isForeignKeyViolation(exception)) {
                throw new DataReferencedException(
                        "외래키 제약조건으로 인해 삭제에 실패했습니다.",
                        "themeId = " + themeId,
                        exception
                );
            }

            throw exception;
        }
    }

    private boolean isForeignKeyViolation(DataIntegrityViolationException exception) {
        String message = exception.getMessage();

        return message != null &&
                message.toLowerCase().contains(FK_RESERVATION_THEME_ID);
    }

    private boolean isDeleted(int deletedRowCount) {
        return deletedRowCount > 0;
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> {
            EntityId id = readEntityId(resultSet, "id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");

            return new Theme(id, name, description, imageUrl);
        };
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);

        return EntityId.fromString(uuid.toString());
    }
}
