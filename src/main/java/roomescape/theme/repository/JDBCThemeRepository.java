package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.entity.ThemeEntity;

@Repository
public class JDBCThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public JDBCThemeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, description, thumbnail FROM theme",
                (resultSet, rowNum) -> {
                    ThemeEntity entity = new ThemeEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    );
                    return entity.toTheme();
                }
        );
    }

    @Override
    public Theme save(final Theme theme) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", theme.getName(),
                        "description", theme.getDescription(),
                        "thumbnail", theme.getThumbnail())).longValue();

        return Theme.of(generatedId, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final long id) {
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id) != 0;
    }

    @Override
    public Optional<Theme> findById(final long id) {
        ThemeEntity themeEntity = jdbcTemplate.queryForObject(
                "SELECT id, name, description, thumbnail FROM theme WHERE id = ?",
                (resultSet, rowNum) -> new ThemeEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id
        );
        return Optional.ofNullable(themeEntity)
                .map(ThemeEntity::toTheme);
    }

    @Override
    public List<Theme> findTop10PopularThemesWithinLastWeek(final LocalDate nowDate) {
        List<ThemeEntity> themeEntity = jdbcTemplate.query(
                "SELECT th.id, th.name, th.description, th.thumbnail "
                        + "FROM theme as th "
                        + "inner join reservation as r "
                        + "on th.id = r.theme_id "
                        + "where PARSEDATETIME(r.date, 'yyyy-MM-dd') between ? and ? "
                        + "group by th.id "
                        + "order by count(r.id) desc, "
                        + "th.name asc "
                        + "limit 10",
                (resultSet, rowNum) -> new ThemeEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), nowDate.minusDays(7), nowDate.minusDays(1)
        );
        return themeEntity.stream()
                .map(ThemeEntity::toTheme)
                .toList();
    }
}
