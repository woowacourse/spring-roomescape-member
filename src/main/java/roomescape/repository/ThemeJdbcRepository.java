package roomescape.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ThemeJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Theme> findById(final long id) {
        var sql = """
            select T.id, T.name, T.description, T.thumbnail from THEME T
            where T.id = ?
            """;

        var themeList = jdbcTemplate.query(sql, RowMappers.THEME, id);
        return themeList.stream().findAny();
    }

    public long save(Theme theme) {
        var insert = new SimpleJdbcInsert(jdbcTemplate);
        var generatedId = insert.withTableName("THEME")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(Map.of(
                "name", theme.name(),
                "description", theme.description(),
                "thumbnail", theme.thumbnail()
            ));
        return generatedId.longValue();
    }

    public boolean removeById(long id) {
        var sql = "delete from THEME where id = ?";

        var removedRowsCount = jdbcTemplate.update(sql, id);
        return removedRowsCount > 0;
    }

    public List<Theme> findAll() {
        var sql = """
            select T.id, T.name, T.description, T.thumbnail from THEME T
            """;

        return jdbcTemplate.query(sql, RowMappers.THEME);
    }
}
