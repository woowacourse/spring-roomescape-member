package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) values (?, ? ,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);
        return new Theme(keyHolder.getKey().longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme where id = ?";
        List<Theme> themes = jdbcTemplate.query(sql, themeRowMapper, id);
        return themes.isEmpty() ? Optional.empty() : Optional.of(themes.get(0));
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public List<Theme> findTop10ByOrderByReservationCountBetween(LocalDate start, LocalDate end) {
        String sql = "select t.id, t.name, t.description, t.thumbnail " +
                "from theme as t inner join reservation as r " +
                "on t.id = r.theme_id " +
                "where r.date between ? and ? " +
                "group by t.id " +
                "order by count(t.id) desc";
        return jdbcTemplate.query(sql, themeRowMapper, Date.valueOf(start), Date.valueOf(end));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            return ps;
        });
    }
}
