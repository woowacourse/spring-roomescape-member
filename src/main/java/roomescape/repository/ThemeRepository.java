package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Duration;
import roomescape.domain.Theme;
import roomescape.exception.InUseEntityException;
import roomescape.repository.dto.ReservedTheme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                + " FROM theme"
                + " ORDER BY id";

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

    public List<ReservedTheme> findMostReserved(
            long limit,
            Duration duration
    ) {
        String findSql = "SELECT t.id, t.name, t.description, t.image_url, count(r.id) AS reservation_count"
                + " FROM theme t"
                + " LEFT OUTER JOIN reservation r"
                + " ON t.id = r.theme_id"
                + " WHERE r.date >= ? AND r.date <= ?"
                + " GROUP BY t.id"
                + " ORDER BY reservation_count DESC"
                + " LIMIT ?";

        LocalDate startDate = duration.startDate();
        LocalDate endDate = duration.endDate();

        return jdbcTemplate.query(
                findSql,
                reservedThemeRowMapper(),
                startDate, endDate,
                limit
        );
    }

    public boolean delete(UUID themeId) {
        String deleteSql = "DELETE FROM theme"
                + " WHERE id = ?";

        try {
            int deletedRowCount = jdbcTemplate.update(deleteSql, themeId.toString());

            return isDeleted(deletedRowCount);
        } catch (DataIntegrityViolationException exception) {
            throw new InUseEntityException(
                    "사용중이지 않은 테마만 제거할 수 있습니다.",
                    "themeId = " + themeId,
                    exception
            );
        }
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
