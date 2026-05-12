package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme findById(Long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";
        return jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id);
    }

    public List<Theme> findAll() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")));
    }

    public List<PopularThemeResult> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                select th.id, th.name, th.description, th.thumbnail, count(r.id) as reservation_count 
                from theme th 
                inner join reservation r 
                on th.id = r.theme_id
                where r.date between ? and ? 
                group by th.id, th.name, th.description, th.thumbnail 
                order by reservation_count desc, th.id asc
                limit ?
                """;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new PopularThemeResult(

                        new Theme(resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail")
                        ),
                        resultSet.getInt("reservation_count")
                )
                , startDate.toString()
                , endDate.toString()
                , limit
        );
    }

    public Theme save(Theme theme) {
        String sql = "insert into theme (name, description, thumbnail) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public void deleteById(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT count(*) FROM theme WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}
