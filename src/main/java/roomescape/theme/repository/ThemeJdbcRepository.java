package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeRepository;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeJdbcRepository(JdbcTemplate jdbcTemplate,
                               @Qualifier("themeJdbcInsert") SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public Theme save(String name, String description, String thumbnail) {

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail);

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Theme(id, name, description, thumbnail);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        ));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Theme> findPopularThemeDuringAWeek(int limit, LocalDate now) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail,
                    COUNT(r.id) AS reservation_count
                FROM
                    theme t
                LEFT JOIN
                    reservation r ON t.id = r.theme_id
                WHERE
                    r.date IS NOT NULL AND
                    PARSEDATETIME(r.date, 'yyyy-MM-dd') >= DATEADD('DAY', -7, PARSEDATETIME(?, 'yyyy-MM-dd'))
                    AND PARSEDATETIME(r.date, 'yyyy-MM-dd') < PARSEDATETIME(?, 'yyyy-MM-dd')
                GROUP BY
                    t.id
                ORDER BY
                    reservation_count DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        ), now, now, limit);
    }
}
