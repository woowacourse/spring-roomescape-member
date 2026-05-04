package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.exception.InUseTimeException;
import roomescape.repository.dto.ReservedTheme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme persist(Theme theme) {
        Number id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "image_url", theme.getImageUrl()
        ));

        return theme.with(id.longValue());
    }

    public Optional<Theme> findById(long id) {
        try {
            String findSql = "SELECT id, name, description, image_url"
                    + " FROM theme"
                    + " WHERE id = ?";

            Theme theme = jdbcTemplate.queryForObject(
                    findSql,
                    themeRowMapper(),
                    id
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservedTheme> findMostReserved(
            long limit,
            LocalDate startDate,
            LocalDate endDate
    ) {
        String findSql = "SELECT t.id, t.name, t.description, t.image_url, count(r.id) AS reservation_count"
                + " FROM theme t"
                + " LEFT OUTER JOIN reservation r"
                + " ON t.id = r.theme_id"
                + " WHERE (? IS NULL OR r.date >= ?) AND r.date <= ?"
                + " GROUP BY t.id"
                + " ORDER BY reservation_count DESC"
                + " LIMIT ?";

        return jdbcTemplate.query(
                findSql,
                reservedThemeRowMapper(),
                startDate, startDate, endDate,
                limit
        );
    }

    public boolean delete(long id) {
        String deleteSql = "DELETE FROM theme"
                + " WHERE id = ?";

        try {
            int deletedRowCount = jdbcTemplate.update(deleteSql, id);

            return deletedRowCount > 0;
        } catch (DataIntegrityViolationException exception) {
            throw new InUseTimeException("사용중이지 않은 시간만 제거할 수 있습니다. id = " + id);
        }
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");

            return Theme.retrieve(id, name, description, imageUrl);
        };
    }

    private RowMapper<ReservedTheme> reservedThemeRowMapper() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");
            int reservationCount = resultSet.getInt("reservation_count");

            return new ReservedTheme(id, name, description, imageUrl, reservationCount);
        };
    }
}
