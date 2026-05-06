package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public void remove(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Theme findById(Long themeId) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String thumbnail = rs.getString("thumbnail");

            return new Theme(id, name, description, thumbnail);
        }, themeId);
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")));
    }

    public List<Long> findAvailableTimes(Long id, LocalDate date) {
        String sql = "SELECT r.time_id FROM reservation r " +
                "JOIN reservation_time rt ON r.time_id = rt.id " +
                "WHERE r.theme_id = ? AND date = ?";


        return jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getLong("id")
        );
    }
}
