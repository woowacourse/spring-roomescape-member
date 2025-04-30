package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeDaoImpl implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcThemeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("theme")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllTheme() {
        String query = "select * from theme";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
            ));
    }

    @Override
    public void saveTheme(Theme theme) {

    }

    @Override
    public void deleteTheme(Long id) {

    }
}
