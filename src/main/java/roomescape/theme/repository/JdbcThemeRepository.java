package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final RowMapper<Theme> ThemeMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        String sql = "insert into theme (name, description, thumbnail_url) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailUrl());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from theme where id = ?", id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";
        List<Theme> results = jdbcTemplate.query(sql, ThemeMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("select id, name from theme", ThemeMapper);
    }

    @Override
    public List<AvailableTimeQueryResult> findAvailableTimes(Long themeId, LocalDate date) {
        String sql = "SELECT rt.id, rt.start_at\n" +
                "FROM reservation_time rt\n" +
                "LEFT JOIN reservation r\n" +
                "  ON rt.id = r.time_id\n" +
                "  AND r.theme_id = ?\n" +
                "  AND r.reservation_date = ?\n" +
                "WHERE r.id IS NULL";

        RowMapper<AvailableTimeQueryResult> reservationTimeMapper = (rs, rowNum) ->
                new AvailableTimeQueryResult(
                        rs.getTime("start_at").toLocalTime()
                );
        return jdbcTemplate.query(sql, reservationTimeMapper, themeId, date);
    }
}
