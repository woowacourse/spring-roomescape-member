package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER =
        (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var description = rs.getString("description");
            var thumbnail = rs.getString("thumbnail");
            return Theme.register(id, name, description, thumbnail);
        };

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

        var themeList = jdbcTemplate.query(sql, THEME_ROW_MAPPER, id);
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

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    public List<Theme> findRankingByPeriod(LocalDate startDate, LocalDate endDate, int limit) {
        var sql = """
            select T.id, T.name, T.description, T.thumbnail from THEME T
            join RESERVATION R on T.id = R.theme_id
            where R.date >= ? and R.date <= ?
            group by T.id
            order by count(R.id) desc
            limit ?
            """;
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER, startDate, endDate, limit);
    }
}
