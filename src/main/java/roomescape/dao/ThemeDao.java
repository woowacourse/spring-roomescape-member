package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.exception.ThemeConstraintException;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(query, mapToTheme());
        return themes;
    }

    public Theme save(Theme theme) {
        String query = "INSERT INTO theme(name, description, thumbnail) VALUES(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query,
                new String[]{"id"});
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public int deleteById(Long id) {
        try {
            String query = "DELETE FROM theme WHERE id = ?";
            return jdbcTemplate.update(query, id);
        } catch (DataIntegrityViolationException e) {
            throw new ThemeConstraintException();
        }
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * from theme where id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, mapToTheme(), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Theme> mapToTheme() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String thumbnail = resultSet.getString("thumbnail");
            return new Theme(id, name, description, thumbnail);
        };
    }

    public int getCountByName(String name) {
        String sql = "SELECT count(*) from theme where name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    public List<Theme> findTop10() {
//        String sql = """
//            select count(*) as count, t.id, t.name, t.description, t.thumbnail
//            from reservation as r
//            inner join theme as t on r.theme_id = t.id
//            where PARSEDATETIME(r.date, 'yyyy-MM-dd') between PARSEDATETIME(TIMESTAMPADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd') and PARSEDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd')
//            group by (t.id)
//            order by count desc
//            limit 10
//            """;
//        String sql = """
//                        select t.id, t.name, t.description, t.thumbnail
//                        from reservation as r
//                        inner join theme as t on r.theme_id = t.id
//                        group by (t.id)
//                        order by (select count(*) where PARSEDATETIME(r.date, 'yyyy-MM-dd') between PARSEDATETIME(TIMESTAMPADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd') and PARSEDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd')) desc
//                        limit 10;
//            """;
//
        String sql = """
                select jj.count, t.id, t.name, t.description, t.thumbnail
                from reservation r
                join (SELECT count(*) as count, id
                      	FROM reservation as re inner join theme as t on re.theme_id = t.id
                      	where t.id = r.theme_id PARSEDATETIME(r.date, 'yyyy-MM-dd') between PARSEDATETIME(TIMESTAMPADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd') and PARSEDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'))
                        group by (theme_id)
                        ) as jj on r.id = re.id
                order by count

        """;
        return jdbcTemplate.query(sql, mapToTheme());
    }
}
