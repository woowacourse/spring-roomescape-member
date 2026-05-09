package roomescape.repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> Theme.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
        );
    }

    @Override
    public Theme save(Theme theme) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        long generatedKey = insert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(theme)
        ).longValue();

        return Theme.of(
                generatedKey,
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select id, name, description, image_url from theme";

        return jdbcTemplate.query(
                sql,
                getThemeRowMapper()
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, image_url from theme where id=?";

        List<Theme> themes = jdbcTemplate.query(
                sql,
                getThemeRowMapper(),
                id
        );
        return themes.stream()
                .findFirst();
    }

    @Override
    public List<Long> findPopularThemeIds() {
        List<Long> popularThemeIds = new LinkedList<>();
        String sql = """
                    SELECT t.id AS theme_id, t.name, COUNT(r.id) AS reservation_count
                    FROM theme AS t
                    LEFT JOIN reservation AS r
                    ON r.theme_id = t.id
                    AND r.date >= ?
                    AND r.date < ?
                    GROUP BY t.id
                    ORDER BY reservation_count DESC, t.name ASC
                    LIMIT 10
                """;

        LocalDate today = LocalDate.now();
        LocalDate beforeOneWeeks = today.minusWeeks(1);

        jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> popularThemeIds.add(resultSet.getLong("theme_id")),
                beforeOneWeeks,
                today
        );
        return popularThemeIds;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from theme where id=?", id);
    }
}


