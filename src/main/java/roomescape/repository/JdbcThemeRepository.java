package roomescape.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final String CREATE_SQL =
            "INSERT INTO `theme`(`name`, `description`, `thumbnail_url`) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM `theme` WHERE `id` = ?";
    private static final String FIND_ALL_SQL =
            "SELECT * FROM `theme`";
    private static final String FIND_RANKING_SQL = """
            SELECT th.id AS id, th.name, th.description,
                   th.thumbnail_url, COUNT(r.id) AS reservation_count
            FROM `theme` th
            LEFT JOIN `reservation` r
              ON r.theme_id = th.id
              AND r.date BETWEEN ? AND ?
            GROUP BY th.id, th.name, th.description, th.thumbnail_url
            ORDER BY reservation_count DESC, th.id ASC
            LIMIT ?""";
    private static final String DELETE_SQL =
            "DELETE FROM `theme` WHERE `id` = ?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme create(Theme themeWithoutId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, new String[]{"id"});
            preparedStatement.setString(1, themeWithoutId.getName());
            preparedStatement.setString(2, themeWithoutId.getDescription());
            preparedStatement.setString(3, themeWithoutId.getThumbnailUrl());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return Theme.of(id, themeWithoutId);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, this::mapToTheme, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, this::mapToTheme);
    }

    @Override
    public List<Theme> findRanking(LocalDate startDate, LocalDate endDate, int limit) {
        return jdbcTemplate.query(FIND_RANKING_SQL, this::mapToTheme, startDate, endDate, limit);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private Theme mapToTheme(ResultSet resultSet, int rowNumber) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String thumbnailUrl = resultSet.getString("thumbnail_url");

        return new Theme(id, name, description, thumbnailUrl);
    }
}
