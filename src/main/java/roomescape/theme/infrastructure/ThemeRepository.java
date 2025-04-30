package roomescape.theme.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeRepository {

    private static RowMapper<Theme> rowMapper = (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Theme theme){
        Map<String,Object> params = new HashMap<>();
        params.put("name",theme.getName());
        params.put("description",theme.getDescription());
        params.put("thumbnail",theme.getThumbnail());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Theme> findAll(){
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql,rowMapper);
    }

    public int deleteById(Long id){
        String sql = "DELETE FROM theme where id =?";

        return jdbcTemplate.update(sql,id);
    }
}
