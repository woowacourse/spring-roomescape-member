package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.model.Theme;

@Repository
public class ThemeDaoImpl implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ThemeDaoImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ));
    }

    @Override
    public Theme addTheme(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return new Theme(newId.longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteTheme(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Theme findThemeById(long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id);
    }

    @Override
    public List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit) {
        String sql = """
                select th.id, th.name, th.description, th.thumbnail 
                from reservation as r 
                inner join theme as th on r.theme_id = th.id 
                where r.date between ? and ? 
                group by r.theme_id 
                order by count(r.theme_id) desc
                limit ? 
                """;
        return jdbcTemplate.query(sql, (resultSet, ignored) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), before, after, limit);
    }
}
