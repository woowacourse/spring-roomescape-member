package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Theme> rowMapper;

    public JdbcThemeRepository(DataSource dataSource, RowMapper<Theme> rowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Theme insertTheme(Theme theme) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        long savedId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findThemeById(savedId);
    }

    @Override
    public void deleteThemeById(long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public boolean isExistThemeOf(long themeId) {
        String sql = "SELECT 1 FROM theme WHERE id = :themeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("themeId", themeId);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    @Override
    public List<Theme> findTopBookedThemes(LocalDate startDate, LocalDate endDate, int themeCount) {
        String sql = """
                    SELECT th.id, th.name, th.description, th.thumbnail
                    FROM theme AS th
                    INNER JOIN reservation AS r ON th.id = r.theme_id
                    WHERE r.date BETWEEN :startDate AND :endDate
                    GROUP BY th.id
                    ORDER BY COUNT(th.id) DESC
                    LIMIT :themeCount;
                """;

        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("themeCount", themeCount);
        return jdbcTemplate.query(sql, paramMap, rowMapper);
    }

    private Theme findThemeById(long savedId) {
        String sql = """
                SELECT * 
                FROM theme AS t 
                WHERE t.id = :savedId;
                """;
        SqlParameterSource paramMap = new MapSqlParameterSource().addValue("savedId", savedId);

        try {
            return jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("아이디가 " + savedId + "인 테마가 존재하지 않습니다.");
        }
    }
}
