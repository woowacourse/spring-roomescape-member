package roomescape.theme.dao;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeJdbcDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(theme);
        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource)
                .longValue();
        theme.setId(id);
        return theme;
    }

    @Override
    public List<Theme> findAll() {
        String findAllThemesSql = "SELECT id, name, description, thumbnail FROM theme";

        return jdbcTemplate.query(findAllThemesSql, (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        ));
    }

    @Override
    public List<Theme> findThemeByDateOrderByThemeIdCount(LocalDate startDate, LocalDate endDate) {
        String findThemesInOrderSql =
                """
                SELECT th.id, th.name, th.thumbnail, th.description
                FROM theme th
                INNER JOIN (
                    SELECT theme_id
                    FROM reservation
                    WHERE date BETWEEN ? AND ?
                    GROUP BY theme_id
                    ORDER BY COUNT(theme_id) DESC
                ) r ON th.id = r.theme_id;
                """;

        return jdbcTemplate.query(findThemesInOrderSql, (resultSet, rowNum)
                        -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
                , startDate.toString(), endDate.toString());
    }

    @Override
    public void deleteById(long themeId) {
        String deleteThemeSql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(deleteThemeSql, themeId);
    }

}
