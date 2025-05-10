package roomescape.repository.reservation;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Theme;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.repository.reservation.mapper.ThemeRowMapper;

@Repository
public class ThemeJdbcDao implements ThemeRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ThemeJdbcDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Theme findById(Long id) {
        String sql = "select * from theme where id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return namedJdbcTemplate.queryForObject(sql, params, ThemeRowMapper.INSTANCE);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("테마 데이터를 찾을 수 없습니다: " + id);
        }
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme order by id;";
        return namedJdbcTemplate.query(sql, ThemeRowMapper.INSTANCE);
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme (name,description,thumbnail) values (:name,:description,:thumbnail)";
        SqlParameterSource params = new MapSqlParameterSource()
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
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);

        if (result == 0) {
            throw new EntityNotFoundException("테마 데이터를 찾을 수 없습니다:" + id);
        }
    }

    @Override
    public List<Theme> findPopularThemes(int period, int maxResults) {
        String sql = """
                select th.id, th.name, th.description, th.thumbnail, count(*) as reservation_count
                from theme as th
                inner join reservation as r
                on th.id = r.theme_id
                where r.date >= DATEADD('DAY', -:period, CURRENT_DATE())
                AND r.date < CURRENT_DATE()
                group by th.id
                order by reservation_count desc
                limit :maxResults
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("period", period)
                .addValue("maxResults", maxResults);
        return namedJdbcTemplate.query(sql, params, ThemeRowMapper.INSTANCE);
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select count(*) from theme where name = :name";
        SqlParameterSource params = new MapSqlParameterSource("name", name);
        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
