package roomescape.domain.theme;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {

    private static final String FIND_ALL_SQL = "select id, name, content, url from theme order by id";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, themeRowMapper());
    }

    private RowMapper<Theme> themeRowMapper() {
        return ((rs, rowNum) -> Theme.of(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("content"),
            rs.getString("url")
        ));
    }
}
