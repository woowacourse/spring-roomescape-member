package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.global.exception.theme.ThemeNotFoundException;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> themeRowMapper = (rs, rowNum) ->
            Theme.from(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url")
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "select * from theme;";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Theme save(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Theme.from(id, theme.getName(), theme.getDescription(), theme.getImageUrl());
    }

    public void deleteById(Long id) {
        String sql = "delete from theme where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedCount = jdbcTemplate.update(sql, parameters);

        if (deletedCount == 0) {
            throw new ThemeNotFoundException("존재하지 않는 테마 번호입니다.");
        }
    }

    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(sql, parameters, themeRowMapper)
                .stream()
                .findFirst();
    }

    public List<Theme> findThemesOrderByReservationCount(LocalDate fromDate,
                                                         LocalDate toDate,
                                                         int limit) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.image_url
                FROM theme t
                INNER JOIN reservation r ON t.id = r.theme_id
                WHERE r.date >= :fromDate
                  AND r.date <= :toDate
                GROUP BY t.id, t.name, t.description, t.image_url
                ORDER BY COUNT(r.id) DESC , t.name
                LIMIT :limit
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("fromDate", fromDate)
                .addValue("toDate", toDate)
                .addValue("limit", limit);

        return jdbcTemplate.query(sql, parameters, themeRowMapper);
    }
}
