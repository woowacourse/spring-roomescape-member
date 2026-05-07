package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        long generatedKey = insert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(theme)
        ).longValue();

        return new Theme(
                generatedKey,
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select id, name, description, image_url from theme";

        return jdbcTemplate.query(
                sql,
                getThemeRowMapper()
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, image_url from theme where id=?";

        List<Theme> themes = jdbcTemplate.query(
                sql,
                getThemeRowMapper(),
                id
        );
        return themes.stream()
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from theme where id=?", id);
    }

    private static RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
        );
    }
}


