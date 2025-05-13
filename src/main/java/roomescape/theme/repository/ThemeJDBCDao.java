package roomescape.theme.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ThemeJDBCDao implements ThemeRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ThemeJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Theme findById(Long id) {
        String sql = "select * from theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedJdbcTemplate.queryForObject(sql, params, getThemeRowMapper());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return namedJdbcTemplate.query(sql, getThemeRowMapper());
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme (name,description,thumbnail) values (:name,:description,:thumbnail)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);

        if (result == 0) {
            throw new EntityNotFoundException("테마 데이터를 찾을 수 없습니다:" + id);
        }
    }

    @Override
    public List<Theme> findPopularThemesThisWeek(LocalDate startInclusive, LocalDate endInclusive, int count) {
        String sql = """
                select th.id, th.name, th.description, th.thumbnail, count(*) as reservation_count
                from theme as th
                inner join reservation as r
                on th.id = r.theme_id
                where r.date between :start and :end
                group by th.id
                order by reservation_count desc
                limit :count
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start", startInclusive)
                .addValue("end", endInclusive)
                .addValue("count", count);

        return namedJdbcTemplate.query(sql, params, getThemeRowMapper());
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
