package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.dto.PopularTheme;
import roomescape.exception.ThemeInUseException;
import roomescape.exception.ThemeNotFoundException;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("img_url")
    );

    private RowMapper<PopularTheme> popularThemeRowMapper = (resultSet, rowNum) -> new PopularTheme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("img_url"),
            resultSet.getLong("theme_rank"),
            resultSet.getLong("reservation_count")
    );

    @Autowired
    public ThemeDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Theme findById(Long id) {
        try {
            String sql = "SELECT * FROM theme WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ThemeNotFoundException("해당 테마를 찾을 수 없습니다.");
        }
    }

    public List<Theme> findAllByIds(List<Long> themeIds) {
        String sql = "SELECT * FROM theme WHERE id IN (:themeIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("themeIds", themeIds);
        return namedParameterJdbcTemplate.query(sql, parameters, themeRowMapper);
    }

    public List<Theme> findAllThemes() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Long insertTheme(String name, String description, String imgUrl) {
        String sql = "INSERT INTO theme (name, description, img_url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql, new String[]{"id"}
            );
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imgUrl);
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        try {
            return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new ThemeInUseException("해당 테마에 예약이 존재합니다.");
        }
    }

    public List<PopularTheme> findPopularThemes(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    ranked.theme_rank,
                    ranked.id,
                    ranked.name,
                    ranked.description,
                    ranked.img_url,
                    ranked.reservation_count
                FROM (
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY COUNT(r.id) DESC, t.id ASC) AS theme_rank,
                        t.id,
                        t.name,
                        t.description,
                        t.img_url,
                        COUNT(r.id) AS reservation_count
                    FROM theme t
                    JOIN reservation r ON t.id = r.theme_id
                    WHERE r.date BETWEEN ? AND ?
                    GROUP BY t.id, t.name, t.description, t.img_url
                ) ranked
                WHERE ranked.theme_rank <= 10
                ORDER BY ranked.theme_rank;
                """;
        return jdbcTemplate.query(sql, popularThemeRowMapper, from, to);
    }
}
