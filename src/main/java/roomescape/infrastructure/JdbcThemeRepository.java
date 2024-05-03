package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.infrastructure.rowmapper.ThemeRowMapper;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingColumns("name", "description", "thumbnail")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme create(Theme theme) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnailUrl());
        long id = jdbcInsert.executeAndReturnKey(parameter)
                .longValue();
        return theme.withId(id);
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql, (rs, rowNum) -> ThemeRowMapper.mapRow(rs));
    }

    @Override
    public Optional<Theme> findById(long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> ThemeRowMapper.mapRow(rs), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Theme getById(long id) throws NoSuchElementException {
        return findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마 입니다."));
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                select t.id, t.name, t.description, t.thumbnail, count(r.id) as reservation_count
                from theme as t left join reservation as r on t.id = r.theme_id
                where r.date between ? and ?
                group by t.id
                order by reservation_count desc
                limit ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> ThemeRowMapper.mapRow(rs), startDate, endDate, limit);
    }
}
