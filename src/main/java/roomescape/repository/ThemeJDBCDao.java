package roomescape.repository;

import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;
import roomescape.exceptions.EntityNotFoundException;

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
        return namedJdbcTemplate.queryForObject(sql, params, getReservationRowMapper());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme (name,description,thumbnail) values (:name,:description,:thumbnail)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("description", theme.description())
                .addValue("thumbnail", theme.thumbnail());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.name(), theme.description(), theme.thumbnail());
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
    public List<Theme> findPopularThemesThisWeek() {
        String sql = """
                select th.id, th.name, th.description, th.thumbnail, count(*) as reservation_count
                from theme as th
                inner join reservation as r
                on th.id = r.theme_id
                where r.date >= CURRENT_DATE() -7
                AND r.date <= CURRENT_DATE()
                group by th.id
                order by reservation_count desc
                limit 10
                """;
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select count(*) from theme where name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Theme> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
