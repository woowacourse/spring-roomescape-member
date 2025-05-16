package roomescape.infra;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.entity.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.error.NotFoundException;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private static final RowMapper<PopularThemeResponse> popularThemeRowMapper = (resultSet, rowNum) ->
            new PopularThemeResponse(
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcThemeRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("theme")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "description", "thumbnail");
    }

    @Override
    public Theme save(final Theme theme) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        final Long newId = inserter.executeAndReturnKey(params).longValue();
        return new Theme(newId, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "select id, name, description, thumbnail from theme";
        return template.query(sql, themeRowMapper);
    }

    @Override
    public List<PopularThemeResponse> findAllPopular() {
        final String sql = """
                    select t.name, t.description, t.thumbnail, count(t.name) as c 
                    from theme t
                    inner join reservation r
                    on r.theme_id = t.id
                    where r.date between
                            current_date - 7
                        and current_date -1
                    group by t.id, t.name, t.description, t.thumbnail
                    order by c desc
                    limit 10;
                """;
        return template.query(sql, popularThemeRowMapper);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from theme where id = ?";
        final int rowsAffected = template.update(sql, id);

        if (rowsAffected != 1) {
            throw new NotFoundException("삭제할 테마가 없습니다. id=" + id);
        }
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = "select id, name, description, thumbnail from theme where id = ?";
        return template.query(sql, themeRowMapper, id)
                .stream()
                .findFirst();
    }
}
