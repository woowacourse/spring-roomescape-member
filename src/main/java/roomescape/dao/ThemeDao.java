package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(query, mapToTheme());
        return themes;
    }

    public Theme save(Theme theme) {
        String query = "INSERT INTO theme(name, description, thumbnail) VALUES(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query,
                new String[]{"id"});
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public int deleteById(Long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * from theme where id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, mapToTheme(), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    private RowMapper<Theme> mapToTheme() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");
            return new Theme(id, name, description, thumbnail);
        };
    }
}
