package roomescape.repository;

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
import roomescape.domain.Theme;
import roomescape.exception.InUseEntityException;
import roomescape.repository.dto.ReservedTheme;

@Repository
public class ThemeRepository {

    private static final String FK_RESERVATION_THEME_ID = "fk_reservation_theme_id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme");
    }

    public Theme persist(Theme theme) {
        simpleJdbcInsert.execute(Map.of(
                "id", theme.id().toString(),
                "name", theme.name(),
                "description", theme.description(),
                "image_url", theme.imageUrl()
        ));

        return theme;
    }

    public List<Theme> findAll() {
        String findSql = "SELECT id, name, description, image_url"
                + " FROM theme";

        return jdbcTemplate.query(findSql, themeRowMapper());
    }

    public Optional<Theme> findById(UUID id) {
        try {
            String findSql = "SELECT id, name, description, image_url"
                    + " FROM theme"
                    + " WHERE id = ?";

            Theme theme = jdbcTemplate.queryForObject(
                    findSql,
                    themeRowMapper(),
                    id.toString()
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Map<UUID, Theme> findByIds(Collection<UUID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }

        String findSql = "SELECT id, name, description, image_url"
                + " FROM theme"
                + " WHERE id IN (:ids)";
        List<String> stringIds = ids.stream()
                .map(UUID::toString)
                .toList();

        List<Theme> themes = namedParameterJdbcTemplate.query(
                findSql,
                Map.of("ids", stringIds),
                themeRowMapper()
        );

        return themes.stream()
                .collect(Collectors.toMap(
                        Theme::id,
                        Function.identity()
                ));
    }

    public boolean delete(UUID themeId) {
        String deleteSql = "DELETE FROM theme"
                + " WHERE id = ?";

        try {
            int deletedRowCount = jdbcTemplate.update(deleteSql, themeId.toString());

            return isDeleted(deletedRowCount);
        } catch (DataIntegrityViolationException exception) {
            if (isForeignKeyViolation(exception)) {
                throw new InUseEntityException(
                        "사용중이지 않은 테마만 제거할 수 있습니다.",
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
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");

            return new Theme(id, name, description, imageUrl);
        };
    }

    private RowMapper<ReservedTheme> reservedThemeRowMapper() {
        return (resultSet, rowNum) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");
            int reservationCount = resultSet.getInt("reservation_count");

            return new ReservedTheme(id, name, description, imageUrl, reservationCount);
        };
    }
}
