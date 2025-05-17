package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;
import roomescape.repository.support.DomainMapper;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "SELECT * FROM THEME";

        return jdbcTemplate.query(sql, DomainMapper.THEME);
    }

    @Override
    public Long save(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ID", theme.id())
                .addValue("NAME", theme.name())
                .addValue("DESCRIPTION", theme.description())
                .addValue("THUMBNAIL", theme.thumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = "SELECT * FROM THEME WHERE ID = ?";
        final List<Theme> themes = jdbcTemplate.query(sql, DomainMapper.THEME, id);

        return themes.stream().findAny();
    }

    @Override
    public Boolean removeById(final Long id) {
        final String sql = "DELETE FROM THEME WHERE ID = ?";
        final int removedRowsCount = jdbcTemplate.update(sql, id);

        return removedRowsCount > 0;
    }
}
