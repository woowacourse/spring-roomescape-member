package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Primary
@Repository
public class JdbcThemeDao implements ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme create(Theme themeWithoutId) {
        String sql = "INSERT INTO `theme`(`name`, `description`, `thumbnail_url`) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, themeWithoutId.getName());
            preparedStatement.setString(2, themeWithoutId.getDescription());
            preparedStatement.setString(3, themeWithoutId.getThumbnailUrl());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return Theme.of(id, themeWithoutId);
    }

    @Override
    public Theme read(Long id) {
        String sql = "SELECT * FROM `theme` WHERE `id` = id";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNumber) -> {
                        String name = resultSet.getString("name");
                        String description = resultSet.getString("description");
                        String thumbnailUrl = resultSet.getString("thumbnail_url");
                        return new Theme(id, name, description, thumbnailUrl);
                    }
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomException(ErrorCode.NOT_FOUND_THEME);
        }
    }

    @Override
    public List<Theme> readAll() {
        String sql = "SELECT * FROM `theme`";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String thumbnailUrl = resultSet.getString("thumbnail_url");
                    return new Theme(id, name, description, thumbnailUrl);
                }
        );
    }
}
