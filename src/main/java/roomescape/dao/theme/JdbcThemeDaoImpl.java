package roomescape.dao.theme;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.theme.InvalidThemeException;
import roomescape.domain.theme.Theme;

@Repository
public class JdbcThemeDaoImpl implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcThemeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllTheme() {
        String query = "select * from theme";
        return jdbcTemplate.query(query, getThemeRowMapper());
    }

    @Override
    public void saveTheme(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        Number newId = insertActor.executeAndReturnKey(parameters);
        theme.setId(newId.longValue());
    }

    @Override
    public void deleteTheme(Long id) {
        String query = "delete from theme where id = ?";

        try {
            int deletedRowCount = jdbcTemplate.update(query, id);
            validateDeleteRowCount(deletedRowCount);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidThemeException("삭제하려는 테마는 이미 예약 되어있는 테마 입니다.");
        }
    }

    private void validateDeleteRowCount(final int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new InvalidThemeException("삭제하려는 ID의 테마가 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(query, getThemeRowMapper(), id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAllThemeOfRankBy(LocalDate startDate, LocalDate currentDate, int limitCount) {
        String query = """
                SELECT id, name, description, thumbnail
                        FROM (
                            SELECT theme_id, COUNT(*) AS reservation_count
                            FROM reservation
                            WHERE date >= ? AND date <= ?
                            GROUP BY theme_id
                        ) AS sub
                        INNER JOIN theme ON sub.theme_id = theme.id
                        ORDER BY sub.reservation_count DESC
                        LIMIT ?
                """;

        return jdbcTemplate.query(query, getThemeRowMapper(), startDate, currentDate, limitCount);
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, RowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
